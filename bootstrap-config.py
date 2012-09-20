#! /usr/bin/env python

import glob
import os
import subprocess
import sys

from optparse import OptionParser

def validate(f, value, msg):
    """Performs an arbitrary validation."""
    if not f(value):
        print >> sys.stderr, 'ERROR: {0}'.format(msg)
        sys.exit(1)

def validate_dir(path, msg):
    """Verifies that a path exists and refers to a directory."""
    validate(os.path.isdir, path, msg)

def validate_file(path, msg):
    """Verifies that a path exists and refers to a regular file."""
    validate(os.path.isfile, path, msg)

def validate_repo(path):
    """Verifies that a git repository exists in a directory."""
    repo = os.path.basename(path)
    msg  = 'A clone of {0} must be available in {1}'.format(repo, path)
    validate_dir(path, msg)
    validate_dir(os.path.join(path, '.git'), msg)

def template_dir(path):
    """Builds the path to the configulon template dir."""
    return os.path.join(path, 'templates')

def envs_file(path):
    """Builds the path to the configulon environments file."""
    return os.path.join(path, 'environments.clj')

def validate_configulon(path):
    """Validates the configulon git repository."""
    validate_repo(path)
    msg = 'The templatized version of configulon is required'
    validate_dir(template_dir(path), msg)
    validate_file(envs_file(path), msg)

def find_clavin_jar(clavin):
    """Finds the JAR file to use when running Clavin."""
    jars = []
    for path in [ clavin, os.path.join(clavin, 'target' ) ]:
        jars.extend(glob.glob(os.path.join(path, '*-standalone.jar')))
    if not jars:
        print >> sys.stderr, "Clavin must be built before running this script"
        sys.exit(1)
    jars.sort()
    return jars[-1]

# Paths that we need.
configulon = os.path.join('..', 'configulon')
clavin     = os.path.join('..', 'Clavin')
prop_dest  = os.path.join('src', 'main', 'resources')

# Do some preliminary validation to ensure that everything looks okay.
validate_configulon(configulon)
validate_repo(clavin)
validate_dir(prop_dest, '{0} does not exist'.format(prop_dest))

# Parse the command-line arguments.
parser = OptionParser(description='Generate test config files.')
parser.add_option('-f', '--envs-file', default = envs_file(configulon),
                  help = 'the path to the environments file')
parser.add_option('-d', '--deployment', default = 'de-2',
                  help = 'the name of the deployment to use')
(options, args) = parser.parse_args()

# Attempt to find the Clavin JAR file.
clavin_jar  = find_clavin_jar(clavin)

# Generate the properties files.
cmd = [
    'java', '-jar', clavin_jar, 'files',
    '-f', options.envs_file,
    '-t', template_dir(configulon),
    '-d', options.deployment,
    '--dest', prop_dest,
    'belphegor', 'belphegor-confluence'
]
subprocess.call(cmd)
