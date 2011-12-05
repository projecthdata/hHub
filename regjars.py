#
# Copyright 2011 The MITRE Corporation
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
#

from string import Formatter
from subprocess import call

#This script adds libraries from the lib folder (which are not available in the public Maven repository) into the local Maven cache
def main():
	libPath = "./lib/"
	
	# a list of dictionaries, each with info about a library to register locally
	#achartengine: http://code.google.com/p/achartengine/
	libs = 	[{'groupId':"org.achartengine", "artifactId":"achartengine", "version":"0.7.0", "file":libPath+"achartengine-0.7.0.jar"}]
	
	#for each library we want to register, get it's dictionary
	for args in libs:
		template = "mvn install:install-file -DgroupId={groupId} -DartifactId={artifactId} -Dversion={version} -Dpackaging=jar -DgeneratePom=true -Dfile={file}"
		#substitute the values into the template. split out the result into an array of command line arguments
		command = template.format(**args).split()
		#make a system call using the array
		call(command)

if __name__ == '__main__':
	main()