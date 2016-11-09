##RichTextView  [![](https://jitpack.io/v/BJChaney/RichTextView.svg)](https://jitpack.io/#BJChaney/RichTextView)

An richtext editor For Android. 

##Download

Maven:

Step 1. Add the JitPack repository to your build file

```
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

Step 2. Add the dependency

```
	<dependency>
	    <groupId>com.github.BJChaney</groupId>
	    <artifactId>RichTextView</artifactId>
	    <version>1.0.1</version>
	</dependency>
```
or Gradle:

Step 1.Add JitPack repository in your root build.gradle at the end of repositories:

```
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```
Step 2. Add the dependency

```
	dependencies {
	        compile 'com.github.BJChaney:RichTextView:1.0.1'
	}
```
##Usage


Add it in your layout xml

```
	<com.chaney.richtextview.RichTextView
        android:id="@+id/richtext_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_below="@+id/btn_select_img"/>
```

Use this method after selected picture

```	
	richTextView.addImageViewByLastFocusIndex(bitmap,url);
```

There are two ways to save richtext

* Use this method get text like  ```xxx#IMG#xxx``` , the ```#IMG#``` is a image tag
	
```
	richTextView.getTemplateRichText();
``` 

* Or use this method get text like ```<p>xxx</p><p><img src="xxx"/></p>```
	
```
	richTextView.getHtmlRichText();
```
	
About to display richtext only support text like ```xxx#IMG#xxx``` 

```
	richTextView.loadingRichText(content,urls);
```
if you need to load image from network, must be added before ```ImageLoader```  and implement ```loadImage``` method

```
	richTextView.setImageLoader(new RichTextView.ImageLoader() {
		@Override
       public void loadImage(ImageView imageView, String url) {
       }
	});
```

More [download demo](https://github.com/BJChaney/RichTextView/tree/master/demo/build/outputs/apk)
##License

	Copyright 2016 BJChaney
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	   http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.