Time zone data utility for OpenJDK
==================================

This utility compiles plain text [IANA time zone database](https://www.iana.org/time-zones)
into `tzbd.dat` binary format supported by OpenJDK 8. It is based on
[code from OpenJDK 8](http://hg.openjdk.java.net/jdk8u/jdk8u/jdk/file/801874e394a7/make/src/classes/build/tools/tzdb),
but doesn't require [NIO.2 API](http://www.jcp.org/en/jsr/detail?id=203) so can be run using OpenJDK 6 or later.
It contains thin (and limited) emulation layer for NIO.2 and regexes with named groups and is trying to
stay as close as possible to upstream jdk8u code.

*Note: due to NIO.2 emulation limitations this utility does not support proper resolving of symbolic links*

Usage
-----

Generate `tzdb.dat` from zoneinfo files:

    mvn clean package
    cd target
    mkdir tzdata
    cd tzdata
    curl -O https://www.iana.org/time-zones/repository/releases/tzdata2015c.tar.gz
    tar xzvf tzdata2015c.tar.gz
    echo tzdata2015c > VERSION
    java -jar ../tzdbgen-1.0.jar -srcdir . -verbose
    ls -l tzdb.dat

*Note: OpenJDK uses additional data files for `tzdb.dat` generation, [details](http://hg.openjdk.java.net/jdk8u/jdk8u/jdk/file/801874e394a7/make/gendata/GendataTZDB.gmk#l32)*

Options:

    Usage: TzdbZoneRulesCompiler <options> <tzdb source filenames>
    where options include:
       -srcdir  <directory>  Where to find tzdb source directory (required)
       -dstfile <file>       Where to output generated file (default srcdir/tzdb.dat)
       -help                 Print this usage message
       -verbose              Output verbose information during compilation
    The source directory must contain the unpacked tzdb files, such as asia or europe

License information
-------------------

This project is released under the [GNU GPL v. 2](https://www.gnu.org/licenses/gpl-2.0.html).
This project uses [named-regexp](https://github.com/tony19/named-regexp) implementation
by [tony19](https://github.com/tony19) released under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).

Changelog
---------

**1.0** (2015-04-20)

 * initial public version
