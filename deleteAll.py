#!/bin/python3
import os

# This script deletes all jars in build/libs from every gradle subproject.

# Get the current directory
currentDir = os.getcwd()

# Get the list of subprojects
# A subproject is a directory in the current directory that contains a build.gradle file
subprojects = os.listdir(currentDir)
subprojects = [sub for sub in subprojects if os.path.isdir(sub)]
subprojects = [sub for sub in subprojects if os.path.isfile(sub + "/build.gradle")]

# Get the list of build/libs directories
subprojectsBuildLibs: list[str] = list(map(lambda sub: sub + "/build/libs", subprojects))
subprojectsBuildLibs = list(filter(lambda sub: os.path.isdir(sub), subprojectsBuildLibs))

# Get the list of jars in build/libs
subprojectsBuildLibsJars: list[tuple[list[str] | list[bytes], str]] = \
    list(map(lambda sub: (os.listdir(sub), sub), subprojectsBuildLibs))
subprojectsBuildLibsJars = list(filter(lambda sub: len(sub[0]) > 0, subprojectsBuildLibsJars))

# Delete the jars to the libs directory
for subprojectBuildLibsJars, subprojectBuildLibs in subprojectsBuildLibsJars:
    for jar in subprojectBuildLibsJars:
        print("Deleting " + jar + " from " + subprojectBuildLibs)
        os.remove(subprojectBuildLibs + "/" + jar)
