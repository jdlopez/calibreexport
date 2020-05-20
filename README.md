# Calibre's Database exporter

## Main goals:

* Export sqlite database of books into something publicable like OPDS

## Current:
 
* First step export books table into JSON format 

## Usage:

First download jar file from releases section. Then execute:

    java -jar calibreexport.jar -db:PATH -out:PATH [-covers:true]
    
Copy result plus [index.html example file](src/test/resources/index.html)

Finally you can publish in any web server.

## Documentation links

- epub metadata format: http://idpf.org/
- OPDS https://opds.io/
- OPDS 1.2 https://specs.opds.io/opds-1.2
- Relax NG for opds 1.2 https://github.com/opds-community/specs/blob/master/schema/1.2/opds.rnc
- Relax NG homepage https://relaxng.org/
