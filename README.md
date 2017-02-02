# jira-csv-bulk-upload

A small utility to batch create JIRA issues from a CSV file. Useful when you don't have Admin rights to use the import wizard.

## Usage

Either build from source or download a [release](https://github.com/leonardoborges/jira-csv-bulk-upload/releases) and run it like so:

    $ java -jar jira-csv-bulk-upload-0.1.0-SNAPSHOT-standalone.jar issues.csv -H https://jira.myorg.com/ -u username -p password
    
For help:

    $ java -jar jira-csv-bulk-upload-0.1.0-SNAPSHOT-standalone.jar --help
    
A sample issues file is provided under `resources/sample.csv`    

## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
