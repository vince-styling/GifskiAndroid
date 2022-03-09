
android opengl 抽帧 vs ffmpeg 抽帧 对比（png 格式）：
帧数：相同
帧质量：目测无差异
帧大小：489kb vs 421kb android opengl 略大，但可接受

BurstLinker(png) vs gifski 生成 gif 对比：
耗时：353982ms vs 569356ms，BurstLinker 更快
同样尺寸，输出 gif 文件大小：81MB vs 43M(desktop) 30M(android) gifski 完胜
输出 gif 文件质量：目测 gifski 略好，但差异不明显，某些帧 BurstLinker 有明显跳动但 gifski 没有

BurstLinker jpg vs png 格式：
耗时：350003ms vs 353982ms，jpg 更快
同样尺寸，输出 gif 文件大小：98MB vs 81M png 更小
输出 gif 文件质量：无差异


03-01 16:01:38.570  7750 20694 I MorphJni: gifski obj valid----
03-01 16:11:01.010  7750 20694 I MorphJni: loop finish:0
(9*60+29)*1000+356=569356

ffmpeg -i demo.mp4 frame%04d.png
gifski -o anim.gif -W 1080 -H 2340 frame*.png

du -sh output.gif
adb pull /storage/emulated/0/Android/data/com.lingyunxiao.gifski/files/output.gif

cargo build --target aarch64-linux-android --release
cp target/aarch64-linux-android/release/libgifski.a ~/dev/GifskiAndroid/gifski/src/main/cpp/arm64-v8a/

cargo build --target armv7-linux-androideabi --release
cp target/armv7-linux-androideabi//release/libgifski.a ~/dev/GifskiAndroid/gifski/src/main/cpp/armeabi-v7a/

gifski v8a so 对比，release 包：
带 jni + android log 的体积：3.6M，压缩打包到 apk 后：1.4MB
只带 android log 的体积：3.3M，压缩打包到 apk 后：1.3MB
只带 jni 的体积：2.7M，压缩打包到 apk 后：1MB
都不带：2.4M，压缩打包到 apk 后：992KB
