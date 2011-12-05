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