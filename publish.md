Add sonatype credential at /home/xxx/.sbt/1.0/sonatype.sbt
```scala
credentials += Credentials(
    "Sonatype Nexus Repository Manager",
    "oss.sonatype.org",
    "sonatypeusername",
    "sonatypepassword"
)
```

Publish the gpg key in keyserver:
```shell
$ gpg --gen-key
$ gpg --list-secret-keys
$ gpg --keyserver https://keys.openpgp.org --send-keys THEKEYTHATYOUSIGNEDTHEJAR
```

publish on staging:
```shell
$ sbt +publishSigned
```
release to public
```shell
$ sbt sonatypeRelease
```
