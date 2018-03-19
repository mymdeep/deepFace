# deepFace
## 前言
deepFace是直接封装了Android原生的图像识别，简化了使用接口，避免了一部分坑，可以获取人脸的中心点，和两眼之间的距离。
## 使用

gradle 依赖
```
compile 'com.deep:face_deep:1.1'
```

## 接口说明

### 拍照识别
拍照识别，要求使用照相机拍照，然后对拍照的内容进行处理。而使用照相机必须要使用一个相机的preview surfaceview 界面，这个已经封装好，可以用deepFace中的`CameraSurfacePreview`,也可以自己写。
> 使用相机必须要要求有个界面展示相机的拍摄内容，否者报错，如果想实现后台偷拍，可以将这个展示的View宽高设置为1像素

如果使用拍照检测，需要初始化相机以及`CameraSurfacePreview`，当然这部分代码用户可以根据需求自己写。

```
mCamera = Camera.open(1);
mCamera.setDisplayOrientation(90);
AndroidDetectUtils.getInstance().setCameraParam(mCamera);
mCameraSurPreview = new CameraSurfacePreview(this,mCamera);
preview.addView(mCameraSurPreview);
```

Camera 0对应的是后置摄像头，1是前置摄像头
setDisplayOrientation是设置相机角度。

### 识别
识别可以直接调用：

```
 AndroidDetectUtils.getInstance().check(bytes, new CheckCallback() {
                            @Override
                            public void onSuccess(int count, Face[] faces) {
                                Log.e("xxxxxx","success "+" count="+count);
                            }

                            @Override
                            public void onFail() {
                                Log.e("xxxxxx","fail ");

                            }
                        });
```
其中bytes是相机拍摄的字节流，也可以传入bitmap或者本地file。

CheckCallback回调，是对检测结果的处理。onSuccess是检测到了人脸，count是人脸个数，Face数组为每个人脸的数据。

>该接口建议在异步线程中调用,如果图片很大，耗时较长。
>

### 特殊接口
#### 设置人脸个数

可以设置最大检测人脸的个数，建议不要超过10，否则影响准确性：

```
Constants.MAX_FACES = 5
```

#### log开关

```
L.debug = true  //false为关闭
```