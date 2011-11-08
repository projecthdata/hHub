#hHub
Official Site: [http://www.projecthdata.org/](http://www.projecthdata.org/)

hHub is an Android app designed to allow for management of chronic conditions and two way communication between users
and remote medical professionals. Using the hData API, it can communicate with a remote medical professional to send
and receive periodic updates on the users condition. While not focused on any particular condition, it provides API
that will allow client apps easily communicate with the remote servers, while only needing to worry about their
particular implementation characteristics.

###What is hData?
hData is a specification for capturing, managing, and exchanging electronic health data.

###What is the objective of hData?
Current electronic health data standards are complex, hard to implement, and difficult to manage, especially for
web centric and mobile developers. hData is a simple, flexible framework that addresses these issues and lays the
foundation for efficient health IT technologies.

The hData specifications have been submitted to Health Layer 7 (HL7) and and the Object Management Group (OMG) for
standadization. The hData Record Format is on the normative track at HL7 and has recently passed the DSTU ballot.
More information can be found in the Standardization page.

##Project Structure

The hHub codebase is managed by the [Maven](http://maven.apache.org//) build tool. It is organized into the following
modules:

*   **hHub-commons:**  a library which includes code that is appplicable to most hHub applications.  This includes hData
    parsing, persistance, and OAuth2 negotiation
*   **hHub-commonds-tests:** unit tests for the hhub-commons library.  This is broken out into a separate project due to
    constraints from the Android Maven plugin
*   **hHub-hData-browser:** an Android application that displays the hData metadata for a given electronic health record
*   **hHub-ehr-viewer:** an Android application that displays the contents of an electronic health record

