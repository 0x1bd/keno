# Keno

Keno is a general-purpose, code-driven animation framework for Kotlin.

## Try it

FFmpeg must be available on `PATH`. Then render the bundled animated typography example:

```shell                                                                                                                          
./gradlew examples:run --args="animated-text"                                                                                     
```                                                                                                                               

The command writes `examples/build/videos/animated-text.mp4`. Pass a second argument to choose
a                                  
different output path:

```shell                                                                                                                          
./gradlew examples:run --args="animated-text /tmp/keno.mp4"                                                                       
```                                                                                                                               

List all bundled examples with `./gradlew examples:run --args="--list"`.

## Build and test

```shell                                                                                                                          
./gradlew test                                                                                                                    
```                                                                                                                               

Skiko is maintained by JetBrains at <https://github.com/JetBrains/skiko>. FFmpeg documentation is
at <https://ffmpeg.org/documentation.html>.           