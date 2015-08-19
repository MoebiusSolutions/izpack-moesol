# IzPack-MoeSol

[IzPack-MoeSol](https://github.com/MoebiusSolutions/izpack-moesol) is a fork of the [IzPack](http://izpack.org/) package that adds some custom features for a particular environment used by customers of Moebius Solutions, Inc. The features added to this module are not considered generally-applicable to IzPack. Any generally-applicable features will be contributed back to the upstream repo.

**License**: IzPack-Moesol applies the same license as IzPack, specifically [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)

**Release Notes**:

* **v5.0.3-moesol-1**
    * Added support for skipping completed Process Panel job that were run on a previous execution
    * Added logging of job titles to the Process Panel

**End of Header**: All information that follows this section is a verbatim copy of the IzPack documentation.  

# IzPack

[IzPack](http://izpack.org/) is a widely used tool for packaging applications on the Java platform as cross-platform installers.

IzPack is part of [The Codehaus](http://codehaus.org/).

## License

IzPack is published under the terms of the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0), meaning that you can adapt it to your needs with very minimal constraints.

Some third-party components (e.g., look and feel libraries) may be released
under different terms.

## Building IzPack from source

[![Build Status](https://secure.travis-ci.org/izpack/izpack.png?branch=master)](http://travis-ci.org/izpack/izpack)

IzPack only requires Java SE 6+ and at minimum Maven 2.2.1 but Maven 3, 3.1 and 3.2 will work too.

    mvn clean install

The build generates a distribution IzPack installer JAR in `izpack-dist/target`.

The IzPack Maven plugin is inside the `izpack-maven-plugin` module.

## Contributing to IzPack

While reporting an issue [on our JIRA tracker](http://jira.codehaus.org/browse/IZPACK) is useful, investigating and offering a patch is much better!

We suggest that [you follow our guidelines for contributing](http://izpack.org/developers/), and especially that you have a fork of [https://github.com/izpack/izpack](https://github.com/izpack/izpack) on GitHub. You can then offer contributions using pull requests.

We very much prefer pull requests over attaching patches in a JIRA issues.

## Resources

* Public web site: [http://izpack.org/](http://izpack.org/)
* IzPack at Codehaus: [http://xircles.codehaus.org/projects/izpack/](http://xircles.codehaus.org/projects/izpack/)
* Mailing-lists: [http://xircles.codehaus.org/projects/izpack/lists](http://xircles.codehaus.org/projects/izpack/lists)
* Wiki and documentation: [http://docs.codehaus.org/display/IZPACK](http://docs.codehaus.org/display/IZPACK)
* News: [http://news.izpack.org/](http://news.izpack.org/)
* Canonical Git repository at The Codehaus: [http://xircles.codehaus.org/projects/izpack/repos/primary/repo](http://xircles.codehaus.org/projects/izpack/repos/primary/repo)
* Git repositories on GitHub, open for community collaboration: [https://github.com/izpack](https://github.com/izpack)
* JIRA issue tracker: [http://jira.codehaus.org/browse/IZPACK](http://jira.codehaus.org/browse/IZPACK)
