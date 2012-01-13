#!/usr/bin/perl

use warnings;
use strict;

use Carp;
use English qw( -no_match_vars );
use File::Basename;
use File::Spec;
use Getopt::Long;

use constant LIB_DIR => File::Spec->catdir( '..', 'lib' );
use constant MAIN_CLASS => 'org.iplantc.de.server.util.GenerateKeystore';
use constant SEC_PROPS  => 'security.properties';
use constant SEC_SETTING =>
    'org.iplantc.discoveryenvironment.securityEnabled';

# Allow command-line option bundling.
Getopt::Long::Configure("bundling_override");

# Get the command-line options.
my $specification = "keystoregenerator.properties";
my $verify_security;
my $opts_ok = GetOptions(
    'specification|s=s' => \$specification,
    'verify-security|v' => \$verify_security,
);
croak usage() if !$opts_ok;

# Get the absolute path to the specifcation file before we change directories.
$specification = File::Spec->rel2abs($specification);

# Ensure that the user is in the correct working directory.
my $dir = dirname $0;
chdir $dir or croak "unable to chdir to $dir: $ERRNO";

# Quit early if we're checking security and security is disabled.
exit if $verify_security && !security_enabled();

# Build the classpath and execute the program.
my $classpath = build_classpath();
system 'java', '-classpath', $classpath, MAIN_CLASS, $specification;

exit $CHILD_ERROR;

##
# Usage      : $enabled = security_enabled();
#
# Purpose    : Determines whether or not security is enabled.
#
# Returns    : True if security is enabled.
#
# Parameters : none.
sub security_enabled {
    my $enabled;

    # Open the file.
    my $file = SEC_PROPS;
    open my $in, '<', $file
        or croak "unable to open $file for input: $ERRNO";

    # Search for the security enabled flag.
    LINE:
    while ( my $line = <$in> ) {
        chomp $line;
        my ( $name, $value ) = split m/\s?=\s?/xms, $line, 2;
        next LINE if $name ne SEC_SETTING;
        $enabled
            = $value =~ m/ \A true \z /ixms  ? 1
            : $value =~ m/ \A false \z /ixms ? 0
            :                                  undef;
        last LINE if defined $enabled;
    }

    # Close the file.
    close $in
        or croak "unable to close $file: $ERRNO";

    # Verify that we found the enabled flag.
    croak "unable to determine if security is enabled; check $file"
        if !defined $enabled;

    return $enabled;
}

##
# Usage      : $usage_message = usage();
#
# Purpose    : Builds and returns the usage message for this utility.
#
# Returns    : The usage message.
#
# Parameters : none.
sub usage {
    my $prog = basename $0;
    return <<"END_OF_USAGE";
Usage:
    $prog [ -s specification | --specification=specification ]
END_OF_USAGE
}

##
# Usage      : $classpath = build_classpath();
#
# Purpose    : Builds and returns the classpath to be used when executing the
#              Java class that implements most of the functionality for this
#              utility.
#
# Returns    : The classpath.
#
# Parameters : none.
sub build_classpath {
    my $lib_dir        = LIB_DIR;
    my $path_separator = ":";
    return ".$path_separator" . join $path_separator, glob "$lib_dir/*.jar";
}
