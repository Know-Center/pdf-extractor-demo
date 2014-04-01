# PDF Extractor Demo

[![CODE](http://code-research.eu/wp-content/uploads/2012/06/code_logo.png)](http://code-research.eu/)
   
[![Know-Center](http://code-research.eu/wp-content/uploads/2012/04/knowcenter_logo_small.png)](http://www.know-center.at)

Simple demo project for the PDF extractor created within the [CODE](http://code-research.eu/team) project by [Know-Center](http://www.know-center.at). It's
a simple command line application that allows you to specify a PDF file, uploads it to the CODE service and
returns a JSON response containing:

  * main text in proper reading order, honoring multi-column PDFs
  * hierarchical table of contents
  * figures & tables

## Building the Demo
To build a runnable JAR invoke Maven in the root directory of the project as follows:

    mvn clean assembly:assembly
    
This will generate a JAR file called `pdf-extractor-jar-with-dependencies.jar` in the `target/` folder.

## Running the Demo
Once the JAR with dependencies is build, you can invoke it as follows from the root directory

    java -jar target/pdf-extractor-jar-with-dependencies.jar <pdf-file>
    
Where `pdf-file` is the path to a locally stored PDF, e.g. `nestin.pdf`, the PDF we ship with this project.

The demo will then upload the specified PDF to the CODE enrichment service. The CODE enrichment service will return
the result as JSON which the demo will write to a file called `<pdf-file>.json`, e.g. `nestin.pdf.json` in the
current working directory.

Additionally, the demo will output some data like the main text, the table of content and the figures & tables
found in the PDF

## License
The contents of this project are licensed under [AGPL 3](http://www.gnu.org/licenses/agpl-3.0.html), see the `LICENSE` 
file.
