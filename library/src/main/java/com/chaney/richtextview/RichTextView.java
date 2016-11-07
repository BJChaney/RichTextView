package com.chaney.richtextview;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chaney on 2016/11/6.
 */

public class RichTextView extends ScrollView {

    //fast double click space time
    private static final long FAST_DOUBLE_CLICK_TIME = 300l;

    //line space size
    private static final float DEFAULT_SPACE_SIZE = 5.0f;

    private static final float DEFAULT_TEXT_SIZE = 15.0f;

    //template flat bit
    private static final String DEFAILT_TAG = "#IMG#";
    private static final String FILT_TAG = "@IMG@";

    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;

    private LinearLayout mRootView;

    private ArrayList<String> mUrls;

    //the edittext lint height
    private int mLineHeight;

    private int editLeftAndRightPadding;

    private float mTextSize;

    private boolean isPreView;

    private int mLastFocusIndex;

    private int mTextColor;

    private float mSpaceSize;


    private ImageLoader imageLoader;

    /**
     * 网络加载回调
     *
     * @param imageLoader
     */
    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public interface ImageLoader {
        void loadImage(ImageView view, String url);
    }

    /**
     * 解析模板字符串
     *
     * @param content 模板字符串 xxx@IMG@xxx
     * @param mUrls 图片urls
     */
    public void loadingRichText(final String content, final List<String> mUrls) {
        post(new Runnable() {// 延迟加载，正确获取控件宽度
            @Override
            public void run() {
                isPreView = true;
                mRootView.removeAllViews();
                String[] texts = content.split(DEFAILT_TAG);
                for (int index = 0; index < texts.length; index++) {
                    String text = texts[index].trim();
                    if (!"".equals(text)) {
                        text = text.replaceAll(FILT_TAG, DEFAILT_TAG);
                        addDefaultEditText(text);
                    }
                    if (index < texts.length - 1) {
                        if(texts.length-1 != mUrls.size()){//not matcher
                            Log.e("TAG--","Error Tag size not match url size");
                        }else {
                            addDefaultImageView(null, mUrls.get(index));
                        }
                    }
                }
            }
        });
    }

    /**
     * 在获取焦点的EditText下方插入ImageView
     *
     * @param bitmap
     * @param url 图片url
     */
    public void addImageViewByLastFocusIndex(Bitmap bitmap, String url) {
        if(isPreView){
            return;
        }
        if(mLastFocusIndex +1 < mRootView.getChildCount()){
            addImageView(bitmap, url, mLastFocusIndex + 1);
        }else {
            addDefaultImageView(bitmap,url);
        }
    }

    /**
     * 在末尾插入ImageView
     *
     * @param bitmap
     * @param url 图片url
     */
    public void addDefaultImageView(Bitmap bitmap, String url) {
        addImageView(bitmap, url, -1);
    }


    /**
     * 获取模板式字符串 xxx@IMG@xxx
     *
     * @return
     */
    public String getTemplateRichText() {
        if (isPreView) {
            return null;
        }
        if(null != mUrls && !mUrls.isEmpty()){
            mUrls.clear();
        }
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < mRootView.getChildCount(); index++) {
            View view = mRootView.getChildAt(index);
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                String text = editText.getText().toString();
                text = text.replaceAll(DEFAILT_TAG, FILT_TAG);
                buffer.append(text);
                buffer.append("\n");
            } else if (view instanceof DeletedImageView) {
                DeletedImageView imageView = (DeletedImageView) view;
                mUrls.add((String) imageView.getTag());
                buffer.append(" ").append(DEFAILT_TAG).append(" ");
            }
        }
        return buffer.toString();
    }

    /**
     * 获取html字符串
     *
     * @return
     */
    public String getHtmlRichText() {
        if (isPreView) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < mRootView.getChildCount(); index++) {
            View view = mRootView.getChildAt(index);

            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                String text = editText.getText().toString();
                if(!"".equals(text)){
                    buffer.append("<p>");
                    text = filtHtmlTag(text);
                    buffer.append(text);
                    buffer.append("</p>");
                }else {
                    buffer.append("<br/>");
                }
            } else if (view instanceof DeletedImageView) {
                DeletedImageView imageView = (DeletedImageView) view;
                String url = (String) imageView.getTag();
                mUrls.add(url);
                buffer.append("<p>").append("<img src=\"").append(url).append("\" />").append("</p>");
            }
        }
        return buffer.toString();
    }

    private String filtHtmlTag(String text) {
        return text.replaceAll("&","&amp;")
                .replaceAll("<","&lt;")
                .replaceAll(">","&gt;")
                .replaceAll("\"","&quot;");
    }


    private void addEditText(String text, int index) {

        final EditText editText = new EditText(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setMinHeight(mLineHeight);
        editText.setBackgroundColor(Color.WHITE);
        editText.setTextColor(getTextColor());
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX,getTextSize());
        editText.setLineSpacing(mSpaceSize, 1);
        editText.setPadding(editLeftAndRightPadding, 0, editLeftAndRightPadding, 0);
        editText.setLayoutParams(params);


        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    addEditTextAfterView(v);
                }
                return true;
            }
        });
        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && mRootView.indexOfChild(v) > 0) {//禁止删除第一个EditText

                    EditText editView = (EditText) v;
                    String str = editView.getText().toString().trim();
                    if ("".equals(str) && OnClickUtil.isFastDoubleClick(FAST_DOUBLE_CLICK_TIME)) {
                        int index = mRootView.indexOfChild(editView);
                        View frontView = mRootView.getChildAt(index - 1);
                        if (frontView instanceof EditText) {
                            frontView.requestFocus();
                            mRootView.removeView(editView);
                        }

                    }
                }
                return false;
            }
        });
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mLastFocusIndex = mRootView.indexOfChild(v);
                }
            }
        });


        if (-1 != index) {
            mRootView.addView(editText, index);
        } else {
            mRootView.addView(editText);
        }

        editText.requestFocus();

        if (isPreView) {
            editText.setEnabled(false);
            editText.setText(text);
        }
    }


    private void addDefaultEditText(String text) {
        addEditText(text, -1);
    }

    private void addEditTextAfterView(TextView v) {
        int index = mRootView.indexOfChild(v);
        if (index + 1 < mRootView.getChildCount()) {
            addEditText("", index + 1);
        } else {
            addDefaultEditText("");
        }
    }



    private void addImageView(Bitmap originalBitmap, String url, int index) {

        DeletedImageView imageView = new DeletedImageView(getContext());
        imageView.setShowDeleteView(!isPreView);

        int width = getWidth();
        if (null != originalBitmap) {
            Bitmap bitmap = ImageUtils.resizeImage(originalBitmap, width);
            imageView.setImageBitmap(bitmap);
        } else {
            if (null != url && !"".equals(url)) {
                if (url.startsWith("content:")) {//local url
                    try {
                        ContentResolver resolver = getContext().getContentResolver();
                        Uri originalUri = Uri.parse(url);
                        Bitmap localBitmap = BitmapFactory.decodeStream(resolver.openInputStream(originalUri));
                        Bitmap bitmap = ImageUtils.resizeImage(localBitmap, width);
                        imageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {//net url
                    if (imageLoader != null) {
                        imageLoader.loadImage(imageView, url);
                    }
                }
            }
        }

        imageView.setTag(url);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = (int) mSpaceSize;
        params.bottomMargin = (int) mSpaceSize;
        imageView.setLayoutParams(params);

        if (-1 != index) {
            mRootView.addView(imageView, index);
        } else {
            mRootView.addView(imageView);
        }

        if (!isPreView) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeImageView(v);
                }
            });
            if (-1 != index) {
                if (index + 1 < mRootView.getChildCount()) {// imageview  not last
                    View v = mRootView.getChildAt(index + 1);
                    if (v instanceof DeletedImageView) {//imageview nextIndex is imageview
                        addEditText("", index + 1);
                    }
                } else {
                    addDefaultEditText("");
                }
            } else {
                addDefaultEditText("");
            }
        }
    }


    private void removeImageView(View view) {
        int index = mRootView.indexOfChild(view);
        if (index + 1 < mRootView.getChildCount()) {
            View nextView = mRootView.getChildAt(index + 1);
            if (nextView instanceof EditText) {
                EditText editText = (EditText) nextView;
                String text = editText.getText().toString().trim();
                if ("".equals(text)) {
                    mRootView.removeView(nextView);
                }
            }
        }
        mRootView.removeView(view);
    }


    private void init(Context context) {
        mRootView = new LinearLayout(context);
        mRootView.setOrientation(LinearLayout.VERTICAL);
        addView(mRootView);

        mUrls = new ArrayList<>();
        isPreView = false;
        mLastFocusIndex = 0;
        editLeftAndRightPadding = ConvertUtil.dp2px(context, DEFAULT_SPACE_SIZE);
        mLineHeight = (int) (getSpaceSize() * 2 + getTextSize());
        setFocusable(false);
        if (!isPreView) {
            addDefaultEditText("");
        }
    }

    public RichTextView(Context context) {
        this(context, null);
    }

    public RichTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(context.obtainStyledAttributes(attrs,R.styleable.RichTextView));
        init(context);
    }

    private void parseAttrs(TypedArray a) {
        if(a!=null){
            try {
                setTextSize(a.getDimension(R.styleable.RichTextView_richTextSize,ConvertUtil.sp2px(getContext(),DEFAULT_TEXT_SIZE)));
                setTextColor(a.getColor(R.styleable.RichTextView_richTextColor,DEFAULT_TEXT_COLOR));
                setSpaceSize(a.getDimension(R.styleable.RichTextView_richSpaceSize,ConvertUtil.dp2px(getContext(),DEFAULT_SPACE_SIZE)));
            }finally {
                a.recycle();
            }
        }
    }

    public ArrayList<String> getUrls() {
        return mUrls;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        this.mTextSize = textSize;
    }

    public float getSpaceSize() {
        return mSpaceSize;
    }

    public void setSpaceSize(float spaceSize) {
        this.mSpaceSize = spaceSize;
    }

}
