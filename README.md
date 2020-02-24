# bachelor data generator

A java program that populates a MySql database with rows randomly based on the MySql structure. Using it to generate data for an experiment in a bachelor thesis project at Högskolan I skövde.bachelor_data_generator. 

A few special cases and interpretations are made to fit with the project that this was specificly designed for:

* varchars and text will fill to roughly 50% of their length (words are generated until they're above half and then cutoff if they went above the lenght).
* Text has a set length of 5000 but due to above rule will end up at around 2500.
* No real relations can be handled instead a comment relation should be added to an int and the generation will have the same upper bound as the amount of rows being created to artificially create some sort of reference to an existing row.
* All tables get the same amount of rows (may change but not likely yet as it is not needed for this project).
* Guids can be created if there is a comment on a varchar (32+) with the comment guid. A uuid will then be pseudorandomly generated and used.

# Dependencies

[mysql-connector.jar](https://static.javatpoint.com/src/jdbc/mysql-connector.jar) ( c:\javalib\mysql-connector.jar)

# Credit

English dictionary csv file is obtained from [here](https://github.com/dwyl/english-words) (words_alpha.txt) and copyright belongs to infochimps [archive](https://web.archive.org/web/20131118073324/http://www.infochimps.com/datasets/word-list-350000-simple-english-words-excel-readable)
