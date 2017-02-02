(ns jira-csv-bulk-upload.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [cheshire.core :as json]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clj-http.client :as client])
  (:gen-class))

(def cli-options [
                  ["-o" "--out PATH"      "Optionally save the generated JSON to a file for debugging"
                   :parse-fn identity]
                  ["-u" "--user USER"     "JIRA Username"
                   :parse-fn identity]
                  ["-p" "--password PASS" "JIRA Password"
                   :parse-fn identity]
                  ["-H" "--host HOST"     "JIRA Host - e.g.: https://jira.myorg.com/"
                   :parse-fn identity]
                  ["-h" "--help"          "Print this help message"]])



(defn issue-spec [issue]
  (let [{:strs [description
                labels
                reporter
                assignee
                issuetype
                summary
                project]} issue]
    {:description description,
     :labels      (clojure.string/split labels #"\s"),
     :reporter    {:name reporter},
     :assignee    {:name assignee},
     :issuetype   {:id issuetype},
     :summary     summary,
     :project     {:id project}}))


(defn csv->jira-json-payload [csv]
  (let [[header & rows]    (with-open [in-file (io/reader csv)]
                             (doall
                              (csv/read-csv in-file)))]
    (->> rows
         (map (partial zipmap header))
         (map issue-spec)
         (map (fn [issue]
                {:fields issue,
                 :update {}}))
         (assoc {} :issueUpdates)
         json/generate-string)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn handle-success [body]
  (println "Success! The following issues have been created:")
  (println "ID  URL")
  (doseq [{:keys [id self]} (-> body (json/parse-string true) :issues)]
    (println id "  " self)))

(defn handle-error [status body]
  (println "Oops! Something went wrong!")
  (println "HTTP Status: "   status)
  (println "Response body: " body))

(defn create-issues [host user password file out]
  (let [json (csv->jira-json-payload file)
        _    (when out (spit out json))]
    (let [{:keys [body status]} (client/post (str host "rest/api/2/issue/bulk") 
                                             {:basic-auth   [user password]
                                              :body         json
                                              :content-type :json})]
      (if (< 200 status 300)
        (handle-success body)
        (handle-error   status body)))))


(defn -main [& [file & rest :as args]]
  (let [{{:keys [out user password help host] :as options} :options
         [file]     :arguments
         summary    :summary} (parse-opts args cli-options)]
    (cond
      help                           (exit 0 summary)
      (not (and user password host)) (exit 1 "Host, user and password are all required.")
      (not file)                     (exit 1 "Input file required.")

      :else (create-issues host user password file out))))


