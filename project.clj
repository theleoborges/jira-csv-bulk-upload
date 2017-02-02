(defproject jira-csv-bulk-upload "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license      {:name "Eclipse Public License"
                 :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main         jira-csv-bulk-upload.core
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.csv "0.1.3"]
                 [cheshire "5.7.0"]
                 [org.clojure/tools.cli "0.3.5"]
                 [clj-http "2.3.0"]])
