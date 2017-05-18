### CommentLinker

This tool makes use of [TagMe](https://tagme.d4science.org/tagme/) to semantically link resources included in a RDF dataset to pertinent DBpedia resources, based on the value of the  ```rdfs:comment``` property.

#### Usage
First you have to build the tool:
```bash
$ mvn package
```

Then, you can run it with the following command:
```bash
$ ./commentlinker <gcube-token> <dataset> [threshold]
```
The threshold should be chosen in the range [0,1]. The default value is 0.15.

The resulting annotations are saved to the file ```annotations.nt```.
