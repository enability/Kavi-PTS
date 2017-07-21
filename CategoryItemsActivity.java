package com.enability.kavipts.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.enability.kavipts.AppConstant;
import com.enability.kavipts.R;
import com.enability.kavipts.controller.FrontController;
import com.enability.kavipts.model.ItemData;
import com.enability.kavipts.persistence.CreateDatabaseAdapter;
import com.enability.kavipts.utilities.DecodeImageUri;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class CategoryItemsActivity extends Activity {

	PowerManager pm;
	WakeLock wl;
	private SharedPreferences sharedpreferences;
	public static final String TAG = "CategoryActivity-KAVIPTS";
	private FrontController frontController;
	private ArrayList<ItemData> categoryDataList;
	private String mLevel;
	private int mCategoryId;
	private int mDisplay_mode;
	private int screenSize;
	private MediaPlayer mediaPlayer, mMediaPlayer2;
	private GridView gridView;
	private ImageAdapter imageAdapter;
	private Context mContext;
	private int mColumnsCount;
	private int mSelected_ID = 0;
	protected String label = "";
	private String mMode = "";
	private LinearLayout relLayout;
	private Animator mCurrentAnimator;
	private int mAnimSpeed;
	/* [START]::Added code to Position issue for Label: Lavanya H M */
	private int level2ItemPosition = 0;
	//	SharedPreferences sharedPreferences;
	public static final String CatPREFERENCES = "catPrefs";
	/* [END]::Added code to Position issue for Label: Lavanya H M */
	private AudioManager mAudioMgr;
	AudioManager.OnAudioFocusChangeListener focusListener;
	private boolean mAnimated = true;
	private Bitmap mAnimBitmap;
	/* [START]::Added code cross hair ScanLine: Lavanya H M */
	private View Horizatal_line;
	private View Verticlel_line;
	private final Handler handler = new Handler();
	private float horizatal_y;
	private float verticle_x;
	private float delta = 1;
	private boolean onclikhorizatalLine = false;
	private boolean onclikVerticleLine = false;
	private float selected_y;
	FrameLayout frameLayout;
	protected float selected_x;
	private int gridscanPos = 0;
	private int gridscanDelta = 0;
	private int mscanSpeed = 40;
	private String defaultspeed = "40";
	private Handler handlerHor = null;
	private Handler handlerVer = null;
	private Runnable mhorizatalLineRunnable, mhoVerticleLineRunnable;
	/* [END]::Added code cross hair ScanLine: Lavanya H M */
	int mGender = AppConstant.GENDER_MALE;
	int mSelectionMode = AppConstant.SEL_CLICK;
	private Handler mTimeHandler = null;
	private Runnable mTimeRunnable, mScanningRunnable, mEraseRunnable;
	private int pictureScanIndex = 0, minPos = 0, maxPos = 0, gridMaxNum = 0;
	private int mPictureHighlightSpeed = 3000;
	private Boolean mPictureScanHilight = false;
	private AdapterView.OnItemClickListener mOnItemClick = null;
	private android.view.ViewGroup.LayoutParams layoutParamsHor;
	private android.view.ViewGroup.LayoutParams layoutParamsVer;
	private int scanLinethickness;
	private String mSelectedLanguage = "en";
	private CreateDatabaseAdapter createDatabaseAdapter;
	private HomeActivity homeActivity;
	String hexColor;
	String username;
	File path = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS);
	String filename_date;
	String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	String time = new SimpleDateFormat("HH:mm:ss").format(new Date());

	private static final String preferencename = "preferences";
	private Editor mEditPrefs;
	String save = "";
	private SharedPreferences mPreferences;
	int i;
	String msg="";
	TextToSpeech ttobj;
	String[] bob = { "Food", "Toilet", "Water", "Wheelchair", "Exercise", "Sleep", "Study", "Drawing", "Eating", "Doctor", "Washing Hands", "Drinking", "Playing", "Brushing", "Throwing Ball", "Writing", "Bathing", "Singing", "Dancing", "ToothBrush", "Spoon", "Glass", "Mirror", "Comb", "Shoe", "Plate", "Towel", "Soap", "Books", "Tomato", "Lemon", "Green Peas", "Carrot", "Cucumber", "Potato", "Ladies Finger", "Onion", "Brinjal", "Radish", "Apple", "Banana", "Papaya", "Watermelon", "Grapes", "Mango", "Strawberry", "Pomegranate", "Guava", "Orange", "Head", "Legs", "Hands", "Nose", "Ear", "Eye", "Teeth", "Tounge", "Mouth", "Stomach", "Milk", "Tea", "Coffee", "Juice", "ButterMilk", "Water", "Pepsi", "Coke", "MilkShake", "Tango Juice", "Dosa", "Idly", "Puri", "Rice", "Chapati", "Dal", "Upama", "Bread", "Pongal", "Lemon Rice", "Home", "Park", "School", "Hospital", "Physiotherapy", "Speech Therapy", "Library", "Restaurant", "Play Ground", "Temple", "Frock", "Pant", "Shirt", "Jacket", "Skirt", "Kurta", "Socks", "T-shirt", "Inner Wear", "Handkerchief"};
	String[] bob2 = {"भोजन", "शौचालय", "पानी", "व्हीलचेयर", "व्यायाम", "नींद", "पढाई करना", "चित्रकारी", "खाना", "चिकित्सक", "हाथ धोना", "खाना खा रहा हूँ", "पीना", "खेलना", "ब्रश करना", "बॉल फेंकना", "लिखना", "नहाना", "गाना", "नाचना", "टूथ ब्रश", "चम्मच", "गिलास", "आईना", "कंघी", "जूता", "प्लेट", "तौलिया", "साबुन", "पुस्तकें", "टमाटर", "निम्बू", "हरी मटर", "गाजर", "खीरा", "आलू", "भिन्डी", "प्याज", "बैंगन", "मूली", "सेब", "केला", "पपीता", "तरबूज", "अंगूर", "आम", "स्ट्रॉबेरी", "अनार", "अमरूद", "नारंगी", "सिर", "पैर", "हाथ", "नाक", "कान", "आंख", "दांत", "जुबान", "मुंह", "जठर", "दूध", "चाय", "कॉफी", "जूस", "छाछ", "पानी", "पेप्सी", "कोक", "मिल्कशेक", "टैंगो का रस", "डोसा", "इडली", "पूरी", "चावल", "चपाती", "दाल", "उपमा", "रोटी", "पोंगल", "नींबू चावल", "निवास", "पार्क", "स्कूल", "अस्पताल", "फिजियोथैरेपी", "वाक  चिकित्सा", "पुस्तकालय", "भोजनालय", "खेल का मैदान", "मंदिर", "फ़राक", "पतलून", "कमीज", "जैकेट", "स्कर्ट", "कुर्ता", "मोज़े", "टी शर्ट", "भीतरी पहनने", "रूमाल"};
//	private Boolean[] replace_audio = new Boolean[100];
	String temp_arr;
	private Boolean[] replace_audio = new Boolean[100];
	private Boolean[] replace_audio1 = new Boolean[100];
	private Boolean[] replace_audio2 = new Boolean[100];
	private Boolean[] replace_audio3 = new Boolean[100];
	String boolean_array_en_lev1;//Prabha to check, track this 4 variables
	String boolean_array_en_lev2;
	String boolean_array1_hi_lev1;
	String boolean_array1_hi_lev2;
	private int defaultScale;
	AudioManager am;
	Settings settingObj;
	ArrayList list = new ArrayList(Arrays.asList(bob));
	ArrayList list2 = new ArrayList(Arrays.asList(bob2));
	boolean s;
//	String boolean_array;
//	String boolean_array1;
	int amStreamMusicMaxVol;
	int amStreamVoiceCall;
	int amStreamCommunication;
	int x;

	byte[] key={'e','n','a','b','i','l','i','t','y','t','w','e','n','t','y','f','o','u','r','f','o','u','n','d','a','t','i','o','n','I','I','T'};
	byte[] iv = {'a','S','S','i','S','t','i','v','e','L','A','B','I','I','T','M'};
	private static boolean replace_picture;
	String temp;

	HashMap<String, String> myHashAlarmVoiceCall = new HashMap();
	HashMap<String, String> myHashAlarmStreamMusic = new HashMap();
	HashMap<String, String> myHashAlarmStreamSystem = new HashMap();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singlecolumn_gridlayout);
		createSpeechObject();
		initializeAudio();
		hideNavigation();

		mContext = this;
		categoryDataList = new ArrayList<ItemData>();
		Bundle data = getIntent().getExtras(); //added by kasthuri to receive the selected scan border color to screen2
		mCategoryId = data.getInt(AppConstant.CATEGORY_ID);
		mLevel = data.getString(AppConstant.LEVEL);
		hexColor = data.getString("hexcolor");
		username = data.getString("user");
		//Default sharedpreferences is changed to sharedprefrences with respect to the username edited by kasthuri to save user settings
		//added by kasthuri
		screenSize = getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		switch (screenSize) {
			case Configuration.SCREENLAYOUT_SIZE_LARGE:
				//		Toast.makeText(this, "Large screen", Toast.LENGTH_LONG).show();
				break;
			case Configuration.SCREENLAYOUT_SIZE_NORMAL:
				//	Toast.makeText(this, "Normal screen", Toast.LENGTH_LONG).show();
				break;
			case Configuration.SCREENLAYOUT_SIZE_SMALL:
				//	Toast.makeText(this, "Small screen", Toast.LENGTH_LONG).show();
				break;
			default:
				//	Toast.makeText(this,
				//		"Screen size is neither large, normal or small",
				//		Toast.LENGTH_LONG).show();
		}
	//	copyAssests();
		replace_audio = loadArray(boolean_array_en_lev1, mContext);
		replace_audio1 = loadArray(boolean_array_en_lev2, mContext);
		replace_audio2 = loadArray(boolean_array1_hi_lev1, mContext);
		replace_audio3 = loadArray(boolean_array1_hi_lev2, mContext);
		sharedpreferences = getSharedPreferences(username, MODE_PRIVATE);

		mAnimSpeed = Integer.parseInt(sharedpreferences.getString("animspeed", "4"));
		createDatabaseAdapter = new CreateDatabaseAdapter(mContext, username);//modified by kasthuri included additional argument to save user settings
		filename_date = username + "_" + date + ".log";

		//	sharedPreferences = getSharedPreferences(CatPREFERENCES,
		//			Context.MODE_PRIVATE);
		/* [START]::Added code to Position issue for Label: Lavanya H M */
		if (mSelectionMode == AppConstant.SEL_CLICK) {
			Editor editor = sharedpreferences.edit();
			editor.putInt(username, 0);
			editor.commit();
			/* [END]::Added code to Position issue for Label: Lavanya H M */
		}
		mSelectedLanguage = sharedpreferences.getString("selection_language", "en");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);

		mDisplay_mode = getResources().getConfiguration().orientation;
		if (mLevel.equalsIgnoreCase("2")
				&& mDisplay_mode == Configuration.ORIENTATION_LANDSCAPE)
			mColumnsCount = Integer
					.parseInt(sharedpreferences.getString(
							"colPref",
							getResources().getString(
									R.string.defValueOfColumn_level2)));
		else
			mColumnsCount = Integer.parseInt(getResources().getString(
					R.string.defValueOfColumn));

		if (mTimeHandler != null) {
			Log.d(TAG, "mTimeRunnable ");
			mTimeHandler.removeCallbacks(mTimeRunnable);
			mTimeHandler.removeCallbacks(mScanningRunnable);
			mTimeHandler.removeCallbacks(mEraseRunnable);
			mTimeHandler = null;
		}

		if (handlerHor != null) {
			handlerHor.removeCallbacks(mhorizatalLineRunnable);
			handlerHor = null;
		}

		if (handlerVer != null) {
			handlerVer.removeCallbacks(mhoVerticleLineRunnable);
			handlerVer = null;
		}
		mPictureScanHilight = false;
		updateUI();
	}

	public boolean storeArray(Boolean[] array, String arrayName, Context mContext) {

		SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(arrayName +"_size", array.length);
		Toast.makeText(getApplicationContext(), "Your arrayName is " + arrayName,  Toast.LENGTH_SHORT).show();
		for(int i=0;i<array.length;i++)
			editor.putBoolean(arrayName + "_" + i, array[i]);

		return editor.commit();
	}

	public Boolean[] loadArray(String arrayName, Context mContext) {

		SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
		int size = prefs.getInt(arrayName + "_size", 100);
		Boolean array[] = new Boolean[size];
		for(int i=0;i<size;i++)
			array[i] = prefs.getBoolean(arrayName + "_" + i, false);

		return array;
	}

	public native String mainfn(String s, String inputtext, String wavname);
	static
	{
		System.loadLibrary("mainfn");
	}

	public void createSpeechObject() {
		ttobj = new TextToSpeech(getApplicationContext(),
				new TextToSpeech.OnInitListener() {
					@Override
					public void onInit(int status) {
						if (status == TextToSpeech.SUCCESS) {
						//	Toast.makeText(getApplication(), "TTS is called...", Toast.LENGTH_LONG).show();
							//	if(ttobj.isLanguageAvailable(new Locale("hi"))==TextToSpeech.LANG_AVAILABLE)
							ttobj.setLanguage(Locale.US);
						//	ttobj.setLanguage(new Locale("hin-IND"));
							//ttobj.setLanguage(new Locale("hin"));
						} else if (status == TextToSpeech.ERROR) {
						//	Toast.makeText(getApplication(), "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
						}
					}
				});
	}
	public void initializeAudio() {
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		amStreamMusicMaxVol = am.getStreamMaxVolume(am.STREAM_MUSIC);
		//am.getStreamVolume(am.STREAM_MUSIC);
		amStreamCommunication = am.getStreamMaxVolume(am.STREAM_SYSTEM);
		amStreamVoiceCall = am.getStreamMaxVolume(am.STREAM_VOICE_CALL);
	}

	public void speakText(String toSpeak) {
		am.setMode(AudioManager.MODE_IN_CALL);
		///////////////////////////////////////////
		x = am.getMode();
		s = am.isSpeakerphoneOn();
		Log.d("bte", "LOUD INCALL :BT NOT ENABLED/WIRED and SPEAKER" + s + x);

		ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, myHashAlarmVoiceCall);


		do {

		} while (ttobj.isSpeaking());
	}

	public void synthesisWavInBackground(String msg) throws IOException {// from share
		//MainActivity me = new MainActivity();
		//	EditText obj1 = (EditText)findViewById(R.id.inputText);
		//	Spinner spk = (Spinner)findViewById(R.id.speaker);
		String speaker_name = "iitm_hindi";
		String inputtext = msg.trim();
		inputtext=inputtext.replace("|",".");
		inputtext=inputtext.replace(" . ", " .");
		inputtext=inputtext.replaceAll("\\s+", " ");
		inputtext=inputtext.trim();
		if(inputtext.endsWith( " ." )){
			inputtext = inputtext.substring(0, inputtext.length() - 2);
		}else if(inputtext.endsWith( " . " )){
			inputtext = inputtext.substring(0, inputtext.length() - 3);
		}
		inputtext = inputtext.trim();
		String foldername = Environment.getExternalStorageDirectory().getPath()+"/Android/data/"+getPackageName().toString()+"/";
		String filename = foldername+speaker_name+".htsvoice";
		String wavname = foldername+"1.wav";
		Toast.makeText(CategoryItemsActivity.this,mainfn(inputtext,filename,wavname),Toast.LENGTH_SHORT).show();
		//	Toast.makeText(getApplicationContext(), inputtext, Toast.LENGTH_SHORT).show();
		/////////////////////////////////////////////////////////////////////////////////
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(wavname);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			mediaPlayer.prepare();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				System.exit(0);
			}
		});
		mediaPlayer.start();

	}


	private void initPictureScanData() {
		// pictureScanIndex = 0;

		// if(mLevel.equalsIgnoreCase("2")){
		// gridMaxNum = 10;
		// }else{
		// gridMaxNum = 4;
		// }

		// gridMaxNum = 9;
		mPictureHighlightSpeed = Integer.parseInt(sharedpreferences.getString(
				"picture_highlight_speed", "3000"));
		Log.d(TAG, "ScanLine grid max is " + gridMaxNum);

		mTimeHandler = new Handler();
		mEraseRunnable = new Runnable() {

			@Override
			public void run() {
				try {
					mPictureScanHilight = false;
					Log.d(TAG, "ScanLine Erased The max pos and min pos are "
							+ maxPos + "  " + minPos + "  " + pictureScanIndex);

					View thumb = gridView.getChildAt(pictureScanIndex - minPos);

					String backColor = sharedpreferences.getString(
							AppConstant.PREF_BACK_COLOR, "#ffFFFF66");
					thumb.setBackgroundColor(Color.parseColor(backColor));
					pictureScanIndex++;
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		};
		mScanningRunnable = new Runnable() {

			@Override
			public void run() {
				minPos = gridView.getFirstVisiblePosition();
				maxPos = gridView.getLastVisiblePosition();
				Log.d(TAG, "ScanLine The max pos : " + maxPos + " min pos:  "
						+ minPos + "pictureScanIndex=" + pictureScanIndex);
				View thumb = gridView.getChildAt(pictureScanIndex - minPos);
				if (thumb != null) {
					mPictureScanHilight = true;
					//thumb.setBackgroundColor(Color.parseColor("#0f0f0f"));

					if(hexColor != null) {//added by kasthuri to change the scanning border color
						//	Toast.makeText(getApplicationContext(), hexColor, Toast.LENGTH_SHORT).show();
						//	thumb.setPadding(4,4,4,4);
						//thumb.setBackgroundColor(Color.parseColor("#00000D"));
						thumb.setBackgroundColor(Color.parseColor(hexColor));
					}else{
						//	Toast.makeText(getApplicationContext(), hexColor, Toast.LENGTH_SHORT).show();
						//thumb.setBackgroundColor(Color.parseColor("#00000D"));
						//	thumb.setPadding(4,4,4,4);
						thumb.setBackgroundColor(Color.parseColor("#0f0f0f"));
					}
				}

			}
		};
		mTimeRunnable = new Runnable() {

			@Override
			public void run() {
				try {
					if (pictureScanIndex <= gridMaxNum) {
						int row = pictureScanIndex / mColumnsCount;
						int selectionId = ((row + 1) * mColumnsCount) - 1;
						gridView.smoothScrollToPosition(selectionId);
						mTimeHandler.postDelayed(this, mPictureHighlightSpeed);
						// [START] fix to defect 99
						mTimeHandler.postDelayed(mScanningRunnable, 100);
						mTimeHandler.postDelayed(mEraseRunnable, 1800);
						// [END] fix to defect 99
						// mTimeHandler.postDelayed(this, 2000);
						// mTimeHandler.postDelayed(mScanningRunnable,100);
						// mTimeHandler.postDelayed(mEraseRunnable, 1900);

					} else {
						// [START] of fix to defect 71
						/*
						 * gridView.smoothScrollToPosition(0); pictureScanIndex
						 * = 0; mTimeHandler.postDelayed(this,
						 * mPictureHighlightSpeed);
						 * mTimeHandler.postDelayed(mScanningRunnable,300);
						 * mTimeHandler.postDelayed(mEraseRunnable, 700);
						 */
						adjustGridView();
						// [END] of fix to defect 71

						// mTimeHandler.postDelayed(this, 2000);
						// mTimeHandler.postDelayed(mScanningRunnable, 100);
						// mTimeHandler.postDelayed(mEraseRunnable, 1900);
					}
				} catch (Exception e) {

				}
			}
		};

		mTimeHandler.postDelayed(mTimeRunnable, mPictureHighlightSpeed);
		// mTimeHandler.postDelayed(mTimeRunnable, 1000);
	}

	@Override
	protected void onResume() {
		super.onResume();
		sharedpreferences = getSharedPreferences(username,
				Context.MODE_PRIVATE);
		System.out.println(">>>>Inside OnResume");
		frontController = FrontController.getInstance(this);
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
		wl.acquire();
		mPictureScanHilight = false;
		mediaPlayer = new MediaPlayer();
		mAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		mDisplay_mode = getResources().getConfiguration().orientation;

		//sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		mMode = sharedpreferences.getString("modes", getResources()
				.getString(R.string.defMode));

		String backColor = sharedpreferences.getString(AppConstant.PREF_BACK_COLOR,
				"#ffFFFF66");
		LinearLayout ll = (LinearLayout) findViewById(R.id.parent_view);
		ll.setBackgroundColor(Color.parseColor(backColor));
		Log.d(TAG, "mMode:::" + mMode);
		if (mLevel.equalsIgnoreCase("2")
				&& mDisplay_mode == Configuration.ORIENTATION_LANDSCAPE)
			mColumnsCount = Integer
					.parseInt(sharedpreferences.getString(
							"colPref",
							getResources().getString(
									R.string.defValueOfColumn_level2)));
		else
			mColumnsCount = Integer.parseInt(getResources().getString(
					R.string.defValueOfColumn));
		mGender = Integer.parseInt(sharedpreferences.getString("gender", "1"));
		mSelectionMode = Integer.parseInt(sharedpreferences.getString("selection_mode", "1"));
		// [START] fix for defect 74
		mscanSpeed = Integer.parseInt(sharedpreferences.getString("scan_speed", defaultspeed));
		if (mSelectionMode == AppConstant.SEL_CROSS_LINE) {
			if (handlerHor != null) {
				handlerHor.removeCallbacks(mhorizatalLineRunnable);
				handlerHor = null;
			}
			if (handlerVer != null) {
				handlerVer.removeCallbacks(mhoVerticleLineRunnable);
				handlerVer = null;
			}
		}
		// [END] fix for defect 74
		updateUI();
		if (mSelectionMode == AppConstant.SEL_CLICK) {
			gridView.setOnItemClickListener(mOnItemClick);
		}
	}
	public static String[] getArray(String input) {
		return input.split("\\|\\$\\|SEPARATOR\\|\\$\\|");
	}

	private void updateUI() {
		//	Toast.makeText(getApplicationContext(),username,Toast.LENGTH_SHORT).show();

		categoryDataList = frontController.fetchCategoryData(mSelectedLanguage,
				mLevel, mCategoryId);
		gridView = (GridView) findViewById(R.id.imagegridView);
		imageAdapter = new ImageAdapter(mContext, username, mSelectedLanguage, categoryDataList,
				mColumnsCount, mCategoryId, screenSize);//modified by kasthuri included additional argument to save user settings
		gridView.setAdapter(imageAdapter);
		gridView.setNumColumns(mColumnsCount);
		if (mLevel.equalsIgnoreCase("2")) {
			gridMaxNum = 10;
		} else {
			gridMaxNum = 4;
		}
		/* [START]::Added code to Position issue for Label: Lavanya H M */
		if (mSelectionMode == AppConstant.SEL_CLICK) {
			level2ItemPosition = sharedpreferences.getInt(username, 0);
			gridView.smoothScrollToPosition(level2ItemPosition);
		}
		// /*[END]::Added code to Position issue for Label: Lavanya H M */
		mOnItemClick = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				ItemData data = null;
				if (mAnimated == true) {
					mAnimated = false;
					//
					try {
						data = (ItemData) view.getTag();
					} catch (NullPointerException e) {
						showAlert(CategoryItemsActivity.this,
								"Wrong Selection", "Please select scanned item");
						return;
					}
					//
					final int category_id = data.getCategory_ID();
					//added by kasthuri to logdata in file
					if (category_id == -1) {
						//	String temp_filename = username + ".log";
						BufferedReader br = null;
						try {
							File path = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS);
							//File file = new File(path, "Kavi.log");
							File file = new File(path, filename_date);
							BufferedWriter writer = new BufferedWriter(new FileWriter(file, true), 8192);
							String writeStr = "backpressed";
							writer.write(writeStr + " " + time + "\n");
							writer.close();
							//Toast.makeText(getBaseContext(), "yes path!!! =)",
							//  Toast.LENGTH_LONG).show();
						}catch (Exception e) {
							//  Toast.makeText(getBaseContext(), "IN EXCEPTION" + e.toString(), Toast.LENGTH_SHORT).show();

						} finally {
							try {
								if (br != null) br.close();
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						}
						finish();
						return;
					}
					if (mediaPlayer != null) {
						if (mediaPlayer.isPlaying()) {
							mediaPlayer.stop();
						}
						mediaPlayer.release();
						if (relLayout != null) {
							relLayout.setVisibility(View.GONE);
						}
					} else {
						mediaPlayer = new MediaPlayer();
					}

					if (mCurrentAnimator != null) {
						if (mCurrentAnimator.isRunning()) {
							mCurrentAnimator.cancel();
							relLayout.setVisibility(View.GONE);
						}
					}
					focusListener = new AudioManager.OnAudioFocusChangeListener() {

						@Override
						public void onAudioFocusChange(int focusChange) {
							switch (focusChange) {
								case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
									if (mediaPlayer != null) {
										mediaPlayer.pause();
									}
									break;
								case AudioManager.AUDIOFOCUS_GAIN:
									// if(mediaPlayer.isPlaying()){
									// mediaPlayer.pause();
									// }
									break;
								case AudioManager.AUDIOFOCUS_LOSS:
									if (mediaPlayer != null) {
										if (mediaPlayer.isPlaying()) {
											mediaPlayer.stop();
										} else {
											mediaPlayer.stop();
										}
										mediaPlayer.release();
									}
							}
						}
					};

					// int audioResult =
					// mAudioMgr.requestAudioFocus(focusListener,
					// AudioManager.STREAM_MUSIC,
					// AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
					//
					// if(audioResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
					// ){
					//
					// ImageView thumb =
					// (ImageView)findViewById(R.id.grid_item_image);
					// String imageuri = data.getImageURI();
					//
					// if(imageuri.contains("android.resource")){
					// zoomImageFromThumb(thumb,Uri.parse(data.getImageURI()),data.getLabel());
					// }else {
					// File imageFile = new File(data.getImageURI());
					// if(imageFile.exists()){
					// zoomImageFromThumb(thumb,Uri.parse(data.getImageURI()),data.getLabel());
					// }else
					// {
					// if(mLevel.equalsIgnoreCase("2")){
					// Log.d(TAG,
					// "The default path is "+CreateDatabaseAdapter.getPicture_lev2(data.getID()-1));
					// frontController.updatePictureData(mLevel,data.getID(),
					// CreateDatabaseAdapter.getPicture_lev2(data.getID()-1));
					// categoryDataList=frontController.fetchHighLevelData(mLevel);
					// zoomImageFromThumb(thumb,Uri.parse(
					// CreateDatabaseAdapter.getPicture_lev2(data.getID()-1)),data.getLabel());
					// }
					// else if (mLevel.equalsIgnoreCase("1")){
					// Log.d(TAG,
					// "The default path is "+CreateDatabaseAdapter.getPicture_lev1(data.getID()-1));
					// frontController.updatePictureData(mLevel,data.getID(),
					// CreateDatabaseAdapter.getPicture_lev1(data.getID()-1));
					// categoryDataList=frontController.fetchHighLevelData(mLevel);
					// zoomImageFromThumb(thumb,Uri.parse(
					// CreateDatabaseAdapter.getPicture_lev1(data.getID()-1)),data.getLabel());
					// }
					// //
					// showAlertOk(mContext,"Missing","The file is not found");
					// // mAnimated = true;
					// }
					// }
					//
					// mediaPlayer = null;
					// mediaPlayer = new MediaPlayer();
					// boolean exceptionOccured = false;
					// try {
					// String audioPath = data.getAudioPath(mGender);
					// if(audioPath.contains("android.resource")){
					// mediaPlayer.setDataSource(mContext,
					// Uri.parse(data.getAudioPath(mGender)));
					// }
					// else{
					// File audioFile = new File(audioPath);
					// if(audioFile.exists()){
					// mediaPlayer.setDataSource(mContext,
					// Uri.parse(data.getAudioPath(mGender)));
					// mediaPlayer.prepare();
					// }
					// else{
					// String audioUri=null;
					// if(mLevel.equalsIgnoreCase("2")){
					// audioUri =
					// CreateDatabaseAdapter.getAudio_lev2(data.getID()-1,mGender);
					// }
					// else if(mLevel.equalsIgnoreCase("1")){
					// audioUri =
					// CreateDatabaseAdapter.getAudio_lev1(data.getID()-1,mGender);
					// }
					//
					// File tempfile = new File(audioUri);
					// if (tempfile.exists())
					// {
					// mediaPlayer.setDataSource(mContext, Uri.parse(audioUri));
					// mediaPlayer.prepare();
					// }else
					// {
					// // Toast.makeText(getApplicationContext(),
					// "Audio file does not exist", Toast.LENGTH_LONG).show();
					// showAlertOk(CategoryItemsActivity.this,"Error!!",
					// "Audio file does not exist");
					// }
					// }
					// }
					// // File audioFile = new File(data.getAudioPath());
					// // mediaPlayer.setDataSource(mContext,
					// Uri.parse(data.getAudioPath()));
					//
					// } catch (IllegalArgumentException e) {
					// Log.i(TAG," the message is "+ e.getMessage());
					// exceptionOccured = true;
					// e.printStackTrace();
					// } catch (IllegalStateException e) {
					// Log.i(TAG," the message is "+ e.getMessage());
					// exceptionOccured = true;
					// e.printStackTrace();
					// } catch (IOException e) {
					// Log.i(TAG," the message is "+ e.getMessage());
					// exceptionOccured = true;
					// e.printStackTrace();
					// }
					// if(exceptionOccured == true){
					// String audioUri=null;
					// if(mLevel.equalsIgnoreCase("2")){
					// audioUri =
					// CreateDatabaseAdapter.getAudio_lev2(data.getID()-1,mGender);
					// try {
					// mediaPlayer.setDataSource(mContext, Uri.parse(audioUri));
					// mediaPlayer.prepare();
					// } catch (IllegalArgumentException e) {
					// e.printStackTrace();
					// } catch (SecurityException e) {
					// e.printStackTrace();
					// } catch (IllegalStateException e) {
					// e.printStackTrace();
					// } catch (IOException e) {
					// e.printStackTrace();
					// }
					// }
					// else if(mLevel.equalsIgnoreCase("1")){
					// audioUri =
					// CreateDatabaseAdapter.getAudio_lev1(data.getID()-1,mGender);
					// try {
					// mediaPlayer.setDataSource(mContext, Uri.parse(audioUri));
					// mediaPlayer.prepare();
					// } catch (IllegalArgumentException e) {
					// e.printStackTrace();
					// } catch (SecurityException e) {
					// e.printStackTrace();
					// } catch (IllegalStateException e) {
					// e.printStackTrace();
					// } catch (IOException e) {
					//
					// e.printStackTrace();
					// }
					// }
					// }

					int audioResult = mAudioMgr.requestAudioFocus(
							focusListener, AudioManager.STREAM_MUSIC,
							AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);

					if (audioResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

						ImageView thumb = (ImageView) findViewById(R.id.grid_item_image);
						String imageuri = data.getImageURI();

						if (imageuri.contains("android.resource")) {
							zoomImageFromThumb(thumb, data.getImageURI(),
									data.getLabel());
						} else {
							File imageFile = new File(data.getImageURI());
							if (imageFile.exists()) {
								zoomImageFromThumb(thumb, data.getImageURI(),
										data.getLabel());
							} else {
								if (mLevel.equalsIgnoreCase("2")) {

									if (mSelectedLanguage
											.equals(AppConstant.OPTIONAL_LANGUAGE_CODE)) {

										frontController
												.updatePictureData(
														mSelectedLanguage,
														mLevel,
														categoryDataList.get(
																position)
																.getID(),
														createDatabaseAdapter
																.opt_lang_getPicture_lev2(data
																		.getID() - 1));
										categoryDataList = frontController
												.fetchCategoryData(
														mSelectedLanguage,
														mLevel, category_id);
										zoomImageFromThumb(thumb,
												createDatabaseAdapter
														.getPicture_lev2(data
																.getID() - 1),
												data.getLabel());

									}else if(mSelectedLanguage
											.equals(AppConstant.OPTIONAL1_LANGUAGE_CODE)){
										frontController
												.updatePictureData(
														mSelectedLanguage,
														mLevel,
														categoryDataList.get(
																position)
																.getID(),
														createDatabaseAdapter
																.opt_lang_getPicture_lev2(data
																		.getID() - 1));
										categoryDataList = frontController
												.fetchCategoryData(
														mSelectedLanguage,
														mLevel, category_id);
										zoomImageFromThumb(thumb,
												createDatabaseAdapter
														.getPicture_lev2(data
																.getID() - 1),
												data.getLabel());
									}else if(mSelectedLanguage
											.equals(AppConstant.OPTIONAL2_LANGUAGE_CODE)){
										frontController
												.updatePictureData(
														mSelectedLanguage,
														mLevel,
														categoryDataList.get(
																position)
																.getID(),
														createDatabaseAdapter
																.opt_lang_getPicture_lev2(data
																		.getID() - 1));
										categoryDataList = frontController
												.fetchCategoryData(
														mSelectedLanguage,
														mLevel, category_id);
										zoomImageFromThumb(thumb,
												createDatabaseAdapter
														.getPicture_lev2(data
																.getID() - 1),
												data.getLabel());
									}else if(mSelectedLanguage
											.equals(AppConstant.OPTIONAL3_LANGUAGE_CODE)){
										frontController
												.updatePictureData(
														mSelectedLanguage,
														mLevel,
														categoryDataList.get(
																position)
																.getID(),
														createDatabaseAdapter
																.opt_lang3_getPicture_lev2(data
																		.getID() - 1));
										categoryDataList = frontController
												.fetchCategoryData(
														mSelectedLanguage,
														mLevel, category_id);
										zoomImageFromThumb(thumb,
												createDatabaseAdapter
														.opt_lang3_getPicture_lev2(data
																.getID() - 1),
												data.getLabel());
									}
									else {

										frontController.updatePictureData(
												mSelectedLanguage, mLevel,
												categoryDataList.get(position)
														.getID(),
												createDatabaseAdapter
														.getPicture_lev2(data
																.getID() - 1));
										categoryDataList = frontController
												.fetchCategoryData(
														mSelectedLanguage,
														mLevel, category_id);
										zoomImageFromThumb(thumb,
												createDatabaseAdapter
														.getPicture_lev2(data
																.getID() - 1),
												data.getLabel());

									}

								} else if (mLevel.equalsIgnoreCase("1")) {
									if (mSelectedLanguage
											.equals(AppConstant.OPTIONAL_LANGUAGE_CODE)) {
										frontController.updatePictureData(
												mSelectedLanguage, mLevel,
												categoryDataList.get(position)
														.getID(),
												createDatabaseAdapter
														.getPicture_lev1(data
																.getID() - 1));
										categoryDataList = frontController
												.fetchCategoryData(
														mSelectedLanguage,
														mLevel, category_id);
										zoomImageFromThumb(thumb,
												createDatabaseAdapter
														.getPicture_lev1(data
																.getID() - 1),
												data.getLabel());
									}else if(mSelectedLanguage
											.equals(AppConstant.OPTIONAL1_LANGUAGE_CODE)){
										frontController.updatePictureData(
												mSelectedLanguage, mLevel,
												categoryDataList.get(position)
														.getID(),
												createDatabaseAdapter
														.getPicture_lev1(data
																.getID() - 1));
										categoryDataList = frontController
												.fetchCategoryData(
														mSelectedLanguage,
														mLevel, category_id);
										zoomImageFromThumb(thumb,
												createDatabaseAdapter
														.getPicture_lev1(data
																.getID() - 1),
												data.getLabel());
									}else if(mSelectedLanguage
											.equals(AppConstant.OPTIONAL2_LANGUAGE_CODE)){
										frontController.updatePictureData(
												mSelectedLanguage, mLevel,
												categoryDataList.get(position)
														.getID(),
												createDatabaseAdapter
														.getPicture_lev1(data
																.getID() - 1));
										categoryDataList = frontController
												.fetchCategoryData(
														mSelectedLanguage,
														mLevel, category_id);
										zoomImageFromThumb(thumb,
												createDatabaseAdapter
														.getPicture_lev1(data
																.getID() - 1),
												data.getLabel());
									}else if(mSelectedLanguage
											.equals(AppConstant.OPTIONAL3_LANGUAGE_CODE)){
										frontController.updatePictureData(
												mSelectedLanguage, mLevel,
												categoryDataList.get(position)
														.getID(),
												createDatabaseAdapter
														.getPicture_lev1(data
																.getID() - 1));
										categoryDataList = frontController
												.fetchCategoryData(
														mSelectedLanguage,
														mLevel, category_id);
										zoomImageFromThumb(thumb,
												createDatabaseAdapter
														.getPicture_lev1(data
																.getID() - 1),
												data.getLabel());
									}
									else {

										Log.i(TAG,
												"The default path is "
														+ createDatabaseAdapter
														.getPicture_lev1(data
																.getID() - 1));
										frontController.updatePictureData(
												mSelectedLanguage, mLevel, data
														.getID(),
												createDatabaseAdapter
														.getPicture_lev1(data
																.getID() - 1));
										categoryDataList = frontController
												.fetchCategoryData(
														mSelectedLanguage,
														mLevel, category_id);
										zoomImageFromThumb(thumb,
												createDatabaseAdapter
														.getPicture_lev1(data
																.getID() - 1),
												data.getLabel());

									}

								}
							}
						}

						mediaPlayer = null;
						mediaPlayer = new MediaPlayer();
						boolean exceptionOccured = false;

						try {
							String audioPath = data.getAudioPath(mGender);
							if (audioPath.contains("android.resource")) {
								mediaPlayer.setDataSource(mContext,
										Uri.parse(data.getAudioPath(mGender)));
							} else {
								File audioFile = new File(audioPath);
								if (audioFile.exists()) {
									//Toast.makeText(getApplicationContext(), "Your i value is " + replace_audio[data.getID()],  Toast.LENGTH_SHORT).show();
									String audioUri = null;
									if(mSelectedLanguage.equals(AppConstant.DEFAULT_LANGUAGE_CODE)) {
										if(mLevel.equalsIgnoreCase("1")) {
											//	Toast.makeText(getApplication(), "inside level " +  msg, Toast.LENGTH_LONG).show();
											if (replace_audio[data.getID()] == Boolean.FALSE) {
												audioUri = null;
												msg = data.getLabel();
												//	Toast.makeText(getApplication(), msg, Toast.LENGTH_LONG).show();
												speakText(msg);
											} else if (replace_audio[data.getID()] == Boolean.TRUE) {
												mediaPlayer.setDataSource(mContext, Uri
														.parse(data.getAudioPath(mGender)));
												mediaPlayer.prepare();
											}
										}else {
											//	Toast.makeText(getApplication(), "inside level 2" +  msg, Toast.LENGTH_LONG).show();
											if (replace_audio1[data.getID()] == Boolean.FALSE) {
												audioUri = null;
												msg = data.getLabel();
												//	Toast.makeText(getApplication(), msg, Toast.LENGTH_LONG).show();
												speakText(msg);
											} else if (replace_audio1[data.getID()] == Boolean.TRUE) {
												mediaPlayer.setDataSource(mContext, Uri
														.parse(data.getAudioPath(mGender)));
												mediaPlayer.prepare();
											}
										}
									}else if(mSelectedLanguage.equals(AppConstant.OPTIONAL2_LANGUAGE_CODE)){
										if(mLevel.equalsIgnoreCase("1")) {
											if (replace_audio2[data.getID()] == Boolean.FALSE) {
												audioUri = null;
												msg = data.getLabel();
												//	Toast.makeText(getApplication(), msg, Toast.LENGTH_LONG).show();
												synthesisWavInBackground(msg);
											} else if (replace_audio2[data.getID()] == Boolean.TRUE) {
												mediaPlayer.setDataSource(mContext, Uri
														.parse(data.getAudioPath(mGender)));
												mediaPlayer.prepare();
											}
										}else{
											if (replace_audio3[data.getID()] == Boolean.FALSE) {
												audioUri = null;
												msg = data.getLabel();
												//	Toast.makeText(getApplication(), msg, Toast.LENGTH_LONG).show();
												synthesisWavInBackground(msg);
											} else if (replace_audio3[data.getID()] == Boolean.TRUE) {
												mediaPlayer.setDataSource(mContext, Uri
														.parse(data.getAudioPath(mGender)));
												mediaPlayer.prepare();
											}
										}
									}else{
										mediaPlayer.setDataSource(mContext, Uri
												.parse(data.getAudioPath(mGender)));
										mediaPlayer.prepare();
									}
								} else {
									String audioUri = null;
									if (mLevel.equalsIgnoreCase("2")) {

										if (mSelectedLanguage
												.equals(AppConstant.OPTIONAL_LANGUAGE_CODE)) {
											audioUri = createDatabaseAdapter
													.opt_lang_getAudio_lev2(
															data.getID() - 1,
															mGender);
										} else if(mSelectedLanguage
												.equals(AppConstant.OPTIONAL1_LANGUAGE_CODE)){
											audioUri = createDatabaseAdapter
													.opt1_lang_getAudio_lev2(
															data.getID() - 1,
															mGender);
										}else if(mSelectedLanguage
												.equals(AppConstant.OPTIONAL2_LANGUAGE_CODE)){
											audioUri = createDatabaseAdapter
													.opt2_lang_getAudio_lev2(
															data.getID() - 1, mGender);
											if(!(list2.contains(data.getLabel()))) {
												audioUri = null;
												TextView txt = (TextView) findViewById(R.id.anim_text);
												msg = data.getLabel();
										//		Toast.makeText(getApplication(), msg, Toast.LENGTH_LONG).show();
												synthesisWavInBackground(msg);
											}
										}else if(mSelectedLanguage
												.equals(AppConstant.OPTIONAL3_LANGUAGE_CODE)){
											audioUri = createDatabaseAdapter
													.opt3_lang_getAudio_lev2(
															data.getID() - 1,
															mGender);
										}
										else {
											audioUri = createDatabaseAdapter
													.getAudio_lev2(
															data.getID() - 1,
															mGender);
											if(!(list.contains(data.getLabel()))) {
												audioUri = null;
												TextView txt = (TextView) findViewById(R.id.anim_text);
												msg = data.getLabel();
										//		Toast.makeText(getApplication(), msg, Toast.LENGTH_LONG).show();
												speakText(msg);
											}
										}
									} else if (mLevel.equalsIgnoreCase("1")) {

										if (mSelectedLanguage
												.equals(AppConstant.OPTIONAL_LANGUAGE_CODE)) {
											audioUri = createDatabaseAdapter
													.opt_lang_getAudio_lev1(
															data.getID() - 1,
															mGender);
										}else if(mSelectedLanguage.equals(AppConstant.OPTIONAL1_LANGUAGE_CODE)){
											audioUri = createDatabaseAdapter
													.opt1_lang_getAudio_lev1(
															data.getID() - 1,
															mGender);
										}else if(mSelectedLanguage
												.equals(AppConstant.OPTIONAL2_LANGUAGE_CODE)){
											audioUri = createDatabaseAdapter
													.opt2_lang_getAudio_lev1(
															data.getID() - 1, mGender);
											if(!(list2.contains(data.getLabel()))) {
												audioUri = null;
												TextView txt = (TextView) findViewById(R.id.anim_text);
												msg = data.getLabel();
										//		Toast.makeText(getApplication(), msg, Toast.LENGTH_LONG).show();
												synthesisWavInBackground(msg);
												//synthesisWavInBackground(msg);
											}
										}else if(mSelectedLanguage.equals(AppConstant.OPTIONAL3_LANGUAGE_CODE)){
											audioUri = createDatabaseAdapter
													.opt3_lang_getAudio_lev1(
															data.getID() - 1,
															mGender);
										}
										else {
												audioUri = createDatabaseAdapter
														.getAudio_lev1(
																data.getID() - 1,
																mGender);
											if(!(list.contains(data.getLabel()))) {
												audioUri = null;
												TextView txt = (TextView) findViewById(R.id.anim_text);
												msg = data.getLabel();
												Toast.makeText(getApplication(), msg, Toast.LENGTH_LONG).show();
												speakText(msg);
											}

							//				Toast.makeText(getApplication(), "am here in if else", Toast.LENGTH_LONG).show();

										}
									}
									try {

										AssetFileDescriptor descriptor = getApplicationContext()
												.getAssets().openFd(audioUri);
										exceptionOccured = false;
										long start = descriptor
												.getStartOffset();
										long end = descriptor.getLength();
										MediaPlayer mediaPlayer = new MediaPlayer();
										mediaPlayer.setDataSource(
												descriptor.getFileDescriptor(),
												start, end);
										mediaPlayer.prepare();
										mediaPlayer.start();
									} catch (Exception e) {
									}
								}
							}
							// mediaPlayer.prepare();
						} catch (IllegalArgumentException e) {
							Log.i(TAG, " the message is " + e.getMessage());
							exceptionOccured = true;
							e.printStackTrace();
						} catch (IllegalStateException e) {
							Log.i(TAG, " the message is " + e.getMessage());
							exceptionOccured = true;
							e.printStackTrace();
						} catch (IOException e) {
							Log.i(TAG, " the message is " + e.getMessage());
							exceptionOccured = true;
							e.printStackTrace();
						}

						if (exceptionOccured == true) {
							String audioPath = data.getAudioPath(mGender);
							File audioFile = new File(audioPath);
							String audioUri = null;
							if (mLevel.equalsIgnoreCase("2")) {
								if (mSelectedLanguage
										.equals(AppConstant.OPTIONAL_LANGUAGE_CODE)) {
									audioUri = createDatabaseAdapter
											.opt_lang_getAudio_lev2(
													data.getID() - 1, mGender);

								}else if(mSelectedLanguage.equals(AppConstant.OPTIONAL1_LANGUAGE_CODE)){
									audioUri = createDatabaseAdapter
											.opt1_lang_getAudio_lev2(
													data.getID() - 1, mGender);
								}else if(mSelectedLanguage
										.equals(AppConstant.OPTIONAL2_LANGUAGE_CODE)){
									audioUri = createDatabaseAdapter
											.opt2_lang_getAudio_lev2(
													data.getID() - 1, mGender);
								}else if(mSelectedLanguage
										.equals(AppConstant.OPTIONAL3_LANGUAGE_CODE)){
									audioUri = createDatabaseAdapter
											.opt3_lang_getAudio_lev2(
													data.getID() - 1, mGender);
								}
								else {
									audioUri = createDatabaseAdapter
											.getAudio_lev2(data.getID() - 1,
													mGender);
								}
								try {
									File tempFile = new File(audioUri);
									if (tempFile.exists()) {
										// mediaPlayer.setDataSource(mContext,
										// Uri.parse(audioUri));
										// mediaPlayer.prepare();
										AssetFileDescriptor descriptor = getApplicationContext()
												.getAssets().openFd(audioUri);
										long start = descriptor
												.getStartOffset();
										long end = descriptor.getLength();
										try {
											mediaPlayer
													.setDataSource(
															descriptor
																	.getFileDescriptor(),
															start, end);
											exceptionOccured = false;
											mediaPlayer.prepare();
											mediaPlayer.start();
										} catch (Exception e) {
										}

									} else {
										mMediaPlayer2 = MediaPlayer.create(
												mContext, R.raw.noaudiofile);
										mMediaPlayer2.start();

									}
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (SecurityException e) {
									e.printStackTrace();
								} catch (IllegalStateException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else if (mLevel.equalsIgnoreCase("1")) {

								if (mSelectedLanguage
										.equals(AppConstant.OPTIONAL_LANGUAGE_CODE)) {
									audioUri = createDatabaseAdapter
											.opt_lang_getAudio_lev1(
													data.getID() - 1, mGender);

								} else if (mSelectedLanguage.equals(AppConstant.OPTIONAL1_LANGUAGE_CODE)) {
									audioUri = createDatabaseAdapter
											.opt1_lang_getAudio_lev1(
													data.getID() - 1, mGender);
								} else if (mSelectedLanguage
										.equals(AppConstant.OPTIONAL2_LANGUAGE_CODE)) {
									audioUri = createDatabaseAdapter
											.opt2_lang_getAudio_lev1(
													data.getID() - 1, mGender);
								}  else if (mSelectedLanguage
										.equals(AppConstant.OPTIONAL3_LANGUAGE_CODE)) {
									audioUri = createDatabaseAdapter
											.opt3_lang_getAudio_lev1(
													data.getID() - 1, mGender);
								} else {
									audioUri = createDatabaseAdapter
												.getAudio_lev1(data.getID() - 1,
														mGender);//Prabha this code i changed but it is creashing

									//Toast.makeText(getApplication(), "am here in if if if", Toast.LENGTH_LONG).show();
								}


								try {

									// Check the Existence of file
									// File tempFile = new File(audioUri);
									// if ( tempFile.exists())
									// {
									// mediaPlayer.setDataSource(mContext,
									// Uri.parse(audioUri));
									// mediaPlayer.prepare();
									AssetFileDescriptor descriptor = getApplicationContext()
											.getAssets().openFd(audioUri);
									long start = descriptor.getStartOffset();
									long end = descriptor.getLength();
									try {
										mediaPlayer.setDataSource(
												descriptor.getFileDescriptor(),
												start, end);
										mediaPlayer.prepare(); //added by kasthuri to fix the no voice output when exceptionoccured turns true
										mediaPlayer.start(); //added by kasthuri to fix the no voice output when exceptionoccured turns true
										exceptionOccured = false;
										// mediaPlayer.prepare();
									} catch (Exception e) {
									}
									// }else
									if (exceptionOccured == true) {
										mMediaPlayer2 = MediaPlayer.create(
												mContext, R.raw.noaudiofile);
										mMediaPlayer2.start();
									}

								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (SecurityException e) {
									e.printStackTrace();
								} catch (IllegalStateException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						 mediaPlayer.start();
						mediaPlayer
								.setOnCompletionListener(new OnCompletionListener() {
									@Override
									public void onCompletion(MediaPlayer mp) {
										mAudioMgr
												.abandonAudioFocus(focusListener);
										if (mAnimated == true) {
											relLayout.setVisibility(View.GONE);
										}
									}
								});

					}
				}
			}
		};

		switch (mSelectionMode) {
			case AppConstant.SEL_CLICK:
				gridView.setOnItemClickListener(mOnItemClick);
				break;
			case AppConstant.SEL_HIGHLIGHT: {
				pictureScanIndex = 0;
				initPictureScanData();
				gridView.setOnItemClickListener(null);
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
											int position, long id) {
						if (mSelectionMode == AppConstant.SEL_HIGHLIGHT
								&& mPictureScanHilight == true) {

							View thumb = gridView.getChildAt(pictureScanIndex
									- minPos);
							mOnItemClick.onItemClick(null, thumb, pictureScanIndex,
									0);
							if (mSelectionMode == AppConstant.SEL_HIGHLIGHT) {
								if (mTimeHandler != null) {
									Log.d(TAG, "mTimeRunnable ");
									mTimeHandler.removeCallbacks(mTimeRunnable);
									mTimeHandler.removeCallbacks(mScanningRunnable);
									mTimeHandler.removeCallbacks(mEraseRunnable);
								}
								mTimeHandler = null;
							}
						}

					}
				});
			}

			break;
			case AppConstant.SEL_CROSS_LINE:
				initScaningLine();

				break;
			default:

				gridView.setOnItemClickListener(mOnItemClick);
				break;
		}

		LinearLayout container = (LinearLayout) findViewById(R.id.parent_view);
		container.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (mSelectionMode) {
					case AppConstant.SEL_HIGHLIGHT: {
						if (mPictureScanHilight) {
							View thumb = gridView.getChildAt(pictureScanIndex
									- minPos);
							mOnItemClick.onItemClick(null, thumb, pictureScanIndex,
									0);

							if (mTimeHandler != null) {
								Log.d(TAG, "mTimeRunnable ");
								mTimeHandler.removeCallbacks(mTimeRunnable);
								mTimeHandler.removeCallbacks(mScanningRunnable);
								mTimeHandler.removeCallbacks(mEraseRunnable);
								mTimeHandler = null;
							}
						}

					}
					break;
					case AppConstant.SEL_CROSS_LINE: {
						if ((onclikhorizatalLine == false)
								&& (onclikVerticleLine == false)) {
							onclikhorizatalLine = true;
							selected_y = Horizatal_line.getY();
							Thread thread = new Thread(new Runnable() {

								@Override
								public void run() {
									animatevericalLine();
								}
							});
							handler.postDelayed(thread, 1000);

						} else if ((onclikhorizatalLine == true)
								&& (onclikVerticleLine == false)) {

							onclikVerticleLine = true;
						}
					}

					break;

					default:

						break;
				}

			}
		});
		//
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				switch (mSelectionMode) {
					case AppConstant.SEL_HIGHLIGHT:

					{
						if (mPictureScanHilight == true) {
							View thumb = gridView.getChildAt(pictureScanIndex
									- minPos);
							mOnItemClick.onItemClick(null, thumb, pictureScanIndex,
									0);
							if (mTimeHandler != null) {
								mTimeHandler.removeCallbacks(mTimeRunnable);
								mTimeHandler.removeCallbacks(mScanningRunnable);
								mTimeHandler.removeCallbacks(mEraseRunnable);
								mTimeHandler = null;
							}
						}

					}
					default:
				}
			}
		});
		//

		// [START] fix for the defect: 123
		gridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (mSelectionMode == AppConstant.SEL_HIGHLIGHT) {
					if ((scrollState == 1)) {
						mPictureScanHilight = false;
						if (mTimeHandler != null) {
							mTimeHandler.removeCallbacks(mTimeRunnable);
							mTimeHandler.removeCallbacks(mScanningRunnable);
							mTimeHandler.removeCallbacks(mEraseRunnable);
							mTimeHandler = null;
						}
						showAlert(mContext, "Picture Scanning - Message",
								"Scrolling is not allowed in picture scan mode.");
					}
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
								 int visibleItemCount, int totalItemCount) {
				Log.d(TAG, " in the onTouch== scrolled");
			}
		});
		// [END] fix for the defect: 123

		gridView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (mSelectionMode) {
					case AppConstant.SEL_CLICK:

						gridView.setOnItemClickListener(mOnItemClick);

					case AppConstant.SEL_HIGHLIGHT:

					{
						View thumb = gridView.getChildAt(pictureScanIndex - minPos);
					/*
					 * if ((mLevel.equalsIgnoreCase("2")) &&
					 * (pictureScanIndex==10)||(mLevel.equalsIgnoreCase("2")) &&
					 * (pictureScanIndex==4)) {
					 */
						if (mPictureScanHilight == true)
							if (((mLevel.equalsIgnoreCase("2")) && (pictureScanIndex == 10))
									|| (((mLevel.equalsIgnoreCase("1"))))) {
								mOnItemClick.onItemClick(null, thumb,
										pictureScanIndex, 0);
								if (mTimeHandler != null) {
									mTimeHandler.removeCallbacks(mTimeRunnable);
									mTimeHandler.removeCallbacks(mScanningRunnable);
									mTimeHandler.removeCallbacks(mEraseRunnable);
									mTimeHandler = null;
								}
								return true;
							}

						return false;
					}

					case AppConstant.SEL_CROSS_LINE: {
						int action = event.getActionMasked();
						if (action == MotionEvent.ACTION_DOWN) {

							if (onclikhorizatalLine == false) {
								onclikhorizatalLine = true;
								selected_y = Horizatal_line.getY();
								animatevericalLine();

							} else if ((onclikhorizatalLine == true)
									&& (onclikVerticleLine == false)) {
								onclikVerticleLine = true;
								selected_x = Verticlel_line.getX();
								selectedItem(selected_x, selected_y);
								onclikhorizatalLine = false;
								onclikVerticleLine = false;

							}
						}
						return true;
					}
					default:
						return false;
				}

			}
		});

		if (mMode.equalsIgnoreCase(getString(R.string.defMode))
				&& (mSelectionMode == AppConstant.SEL_CLICK)) {
			Log.d(TAG, ">>>Inside register for Context Menu");
			registerForContextMenu(gridView);
		} else {
			unregisterForContextMenu(gridView);
		}
	}

	public void initScaningLine() {

		Horizatal_line = (View) findViewById(R.id.horizatal_line);
		Verticlel_line = (View) findViewById(R.id.verticle_line);
		Horizatal_line.setVisibility(View.VISIBLE);
		Verticlel_line.setVisibility(View.VISIBLE);
		Horizatal_line.setY(0);
		Verticlel_line.setX(0);

		String scanLineColor = sharedpreferences.getString(AppConstant.PREF_SCANLINE_COLOR,
				"#000000");
		mscanSpeed = Integer.parseInt(sharedpreferences.getString("scan_speed", defaultspeed));
		Horizatal_line.setBackgroundColor(Color.parseColor(scanLineColor));
		Verticlel_line.setBackgroundColor(Color.parseColor(scanLineColor));
		gridscanPos = 0;
		gridscanDelta = 0;
		// [START] of fix to defect 103
		scanLinethickness = Integer.parseInt(sharedpreferences.getString(
				AppConstant.PREF_SCANLINE_THICKNESS, "5"));
		// [END] of fix to defect 103
		layoutParamsHor = Horizatal_line.getLayoutParams();
		layoutParamsHor.height = scanLinethickness;
		Horizatal_line.setLayoutParams(layoutParamsHor);
		layoutParamsVer = Verticlel_line.getLayoutParams();
		layoutParamsVer.width = scanLinethickness;
		Verticlel_line.setLayoutParams(layoutParamsVer);
		onclikhorizatalLine = false;
		onclikVerticleLine = false;
		animateHorizatalLine();
	}

	private void animateHorizatalLine() {

		handlerHor = new Handler();
		mhorizatalLineRunnable = new Runnable() {

			public void run() {
				try {
					horizatal_y = Horizatal_line.getY();
					horizatal_y = horizatal_y + delta;
					Horizatal_line.setY(horizatal_y);

					if ((horizatal_y <= gridView.getBottom())
							&& (onclikhorizatalLine == false)) {
						handlerHor.removeCallbacks(mhorizatalLineRunnable);
						handlerHor.postDelayed(mhorizatalLineRunnable,
								mscanSpeed);

					} else if ((horizatal_y > gridView.getBottom())
							&& (onclikhorizatalLine == false)) {

						if ((gridView.getLastVisiblePosition() < gridMaxNum)
								|| (gridscanPos < gridMaxNum)) {

							if (gridscanDelta == 0) {
								gridscanDelta = mColumnsCount + 1;
								gridscanPos = gridView.getLastVisiblePosition();
							}
							if (gridscanPos == 0) {
								gridscanPos = gridView.getLastVisiblePosition();
							}
							gridscanPos = gridscanPos + gridscanDelta;
							if (gridscanPos >= gridView.getCount()) {
								gridscanPos = gridMaxNum;
							}
							gridView.smoothScrollToPosition(gridscanPos);
						} else {
							adjustGridView();
						}
						horizatal_y = gridView.getTop();
						Horizatal_line.setY(horizatal_y);
						handlerHor.removeCallbacks(mhorizatalLineRunnable);
						handlerHor.postDelayed(mhorizatalLineRunnable,
								mscanSpeed);
					}
					Horizatal_line.invalidate();
				} catch (Throwable t) {

				}
			}
		};

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				if (mSelectionMode == AppConstant.SEL_CROSS_LINE) {
					if (onclikhorizatalLine == false) {
						onclikhorizatalLine = true;
						selected_y = Horizatal_line.getY();
						animatevericalLine();
					}
				}
			}
		});

		frameLayout = (FrameLayout) findViewById(R.id.container);
		frameLayout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (mSelectionMode == AppConstant.SEL_CROSS_LINE) {
					if (onclikhorizatalLine == false) {
						onclikhorizatalLine = true;
						selected_y = Horizatal_line.getY();
						animatevericalLine();

					} else if ((onclikhorizatalLine == true)
							&& (onclikVerticleLine == false)) {
						onclikVerticleLine = true;
					}
				}
			}
		});

		Horizatal_line.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (mSelectionMode == AppConstant.SEL_CROSS_LINE) {
					if (onclikhorizatalLine == false) {
						onclikhorizatalLine = true;
						selected_y = Horizatal_line.getY();
						animatevericalLine();
					} else if ((onclikhorizatalLine == true)
							&& (onclikVerticleLine == false)) {

						onclikVerticleLine = true;
					}
				}
			}
		});

		handler.postDelayed(mhorizatalLineRunnable, mscanSpeed);
	}

	private void animatevericalLine() {
		handlerVer = new Handler();

		mhoVerticleLineRunnable = new Runnable() {

			public void run() {
				try {

					verticle_x = Verticlel_line.getX();
					verticle_x = verticle_x + delta;
					Verticlel_line.setX(verticle_x);
					if ((verticle_x <= gridView.getWidth())
							&& (onclikhorizatalLine == true)
							&& (onclikVerticleLine == false)) {

						handler.postDelayed(mhoVerticleLineRunnable, mscanSpeed);

					} else if ((verticle_x > gridView.getWidth())
							&& (onclikhorizatalLine == true)
							&& (onclikVerticleLine == false)) {

						verticle_x = gridView.getLeft();
						Verticlel_line.setX(verticle_x);
						handlerVer.removeCallbacks(mhoVerticleLineRunnable);
						handlerVer.postDelayed(mhoVerticleLineRunnable,
								mscanSpeed);

					}
					Verticlel_line.invalidate();

				} catch (Throwable t) {

				}
			}
		};

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				if (mSelectionMode == AppConstant.SEL_CROSS_LINE) {
					if ((onclikhorizatalLine == true)
							&& (onclikVerticleLine == false)) {

						onclikVerticleLine = true;
						selected_x = Verticlel_line.getX();
						selectedItem(selected_x, selected_y);
						onclikhorizatalLine = false;
						onclikVerticleLine = false;
					}
				}
			}
		});

		frameLayout = (FrameLayout) findViewById(R.id.container);
		frameLayout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (mSelectionMode == AppConstant.SEL_CROSS_LINE) {
					if ((onclikhorizatalLine == true)
							&& (onclikVerticleLine == false)) {

						onclikVerticleLine = true;
						selected_x = Verticlel_line.getX();
						selectedItem(selected_x, selected_y);
						onclikhorizatalLine = false;
						onclikVerticleLine = false;
					}
				}
			}
		});

		Horizatal_line.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (mSelectionMode == AppConstant.SEL_CROSS_LINE) {
					if ((onclikhorizatalLine == true)
							&& (onclikVerticleLine == false)) {

						onclikVerticleLine = true;
						selectedItem(selected_x, selected_y);
						onclikhorizatalLine = false;
						onclikVerticleLine = false;
					}
				}
			}
		});

		Verticlel_line.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (mSelectionMode == AppConstant.SEL_CROSS_LINE) {
					if ((onclikhorizatalLine == true)
							&& (onclikVerticleLine == false)) {

						onclikVerticleLine = true;
						selected_x = Verticlel_line.getX();
						selectedItem(selected_x, selected_y);
						onclikhorizatalLine = false;
						onclikVerticleLine = false;

					}
				}
			}
		});
		handlerVer.removeCallbacks(mhoVerticleLineRunnable);
		handlerVer.postDelayed(mhoVerticleLineRunnable, mscanSpeed);
	}

	public void selectedItem(float selected_x2, float selected_y2) {

		LinearLayout ll = (LinearLayout) findViewById(R.id.parent_view);

		System.out.println("gridView.getHeight()" + gridView.getHeight());
		System.out.println("gridView.getBottom()" + gridView.getBottom());
		System.out.println("frameLayout.getHeight()" + frameLayout.getHeight());
		System.out.println("ll.getHeight()" + ll.getHeight());

		int position = gridView.pointToPosition((int) selected_x,
				(int) selected_y);

		int FirstVisiblePosition = gridView.getFirstVisiblePosition();
		View thumb = gridView.getChildAt(position - FirstVisiblePosition);

		if (thumb == null) {

			// [START] fix for defect 97
			showAlertOk(mContext, "Wrong Selection",
					"Empty space selection is not allowed");
			// [END] fix for defect 97
			onclikhorizatalLine = false;
			onclikVerticleLine = false;
			// [Start] fix to defect 102
			if (handlerHor != null) {
				handlerHor.removeCallbacks(mhorizatalLineRunnable);
				handlerHor = null;
			}
			if (handlerVer != null) {
				handlerVer.removeCallbacks(mhoVerticleLineRunnable);
				handlerVer = null;
			}
			initScaningLine();
			updateUI();
			// [END] fix to defect 102

		} else {
			mOnItemClick.onItemClick(null, thumb, position, 0);
		}
	}

	@SuppressLint("NewApi")
	public void zoomImageFromThumb(View thumbView, String uri,
								   CharSequence text_label) {

		final FrameLayout frmLayout = (FrameLayout) findViewById(R.id.container);

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		relLayout = (LinearLayout) inflater.inflate(R.layout.anim_object, null);
		ImageView expandedImage = (ImageView) relLayout
				.findViewById(R.id.anim_image);
		TextView labelText = (TextView) relLayout.findViewById(R.id.anim_text);
		labelText.setText(text_label);
		// [START] code for Label color change feature
		String labelTextColor = sharedpreferences.getString(AppConstant.PREF_LABEL_COLOR,
				"#0f0f0f");//added by kasthuri changed the default label color to black
		labelText.setTextColor(Color.parseColor(labelTextColor));
		// [END] code for Label color change feature

		String writeStr = uri;
		String[] separated = writeStr.split("/");
		//	String filename = username + ".log";
		File file = new File(path, filename_date);
		BufferedReader br = null;		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true), 8192);
			writer.write(separated[3] + " " + time + "\n");//printing levels
			writer.close();
			//Toast.makeText(getBaseContext(), "yes path!!! =)",
			//  Toast.LENGTH_LONG).show();
		}catch (Exception e) {
			//  Toast.makeText(getBaseContext(), "IN EXCEPTION" + e.toString(), Toast.LENGTH_SHORT).show();

		}
		final FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				Gravity.CENTER);

		frmLayout.addView(relLayout, fp);

		final Rect thumbBounds = new Rect();
		final Rect startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();
		int intrinsicHeight, intrinsicWidth;

		Drawable drawable = expandedImage.getDrawable();
		if (drawable != null) {
			intrinsicHeight = drawable.getIntrinsicHeight();
			intrinsicWidth = drawable.getIntrinsicWidth();
		} else {
			intrinsicHeight = 100;// to fix the crash during the image replace.
			intrinsicWidth = 100;
		}

		thumbView.getGlobalVisibleRect(thumbBounds);
		relLayout.getGlobalVisibleRect(startBounds);
		frmLayout.getGlobalVisibleRect(finalBounds, globalOffset);

		startBounds.offset(-globalOffset.x, -globalOffset.y);
		finalBounds.offset(-globalOffset.x, -globalOffset.y);
		thumbBounds.offset(-globalOffset.x, -globalOffset.y);
		//	Toast.makeText(getBaseContext(), "this is my Toast message!!! =)",
		//			Toast.LENGTH_LONG).show();//added by kasthuri statement works for each clicks in sublevel
		Log.d(TAG, "global offset :::" + globalOffset.x + ":" + globalOffset.y);
		Log.d(TAG, "thumb rectangle :::" + thumbBounds.left + ":"
				+ thumbBounds.top + ":" + thumbBounds.right + ":"
				+ thumbBounds.bottom);
		Log.d(TAG, "start rectangle :::" + startBounds.left + ":"
				+ startBounds.top + ":" + startBounds.right + ":"
				+ startBounds.bottom);
		Log.d(TAG, "final rectangle :::" + finalBounds.left + ":"
				+ finalBounds.top + ":" + finalBounds.right + ":"
				+ finalBounds.bottom);

		Log.d(TAG, "THUMB RECTANGLe dimensions" + thumbBounds.width() + ":"
				+ thumbBounds.height());
		Log.d(TAG, "start bound dimensions" + startBounds.width() + ":"
				+ startBounds.height());
		Log.d(TAG, "final bound dimensions" + finalBounds.width() + ":"
				+ finalBounds.height());

		if (uri.contains("android.resource")) {
			expandedImage.setImageURI(Uri.parse(uri));
		} else {

			mAnimBitmap = DecodeImageUri.decodeSampledBitmapFromAssets(
					mContext, uri, 200, 200);
			expandedImage.setImageBitmap(mAnimBitmap);
			/*intrinsicHeight = 100;// to fix the crash during the image replace.
			intrinsicWidth = 100;*/
			if (!(null == mAnimBitmap)) {
				intrinsicHeight = mAnimBitmap.getHeight();
				intrinsicWidth = mAnimBitmap.getWidth();
			}
		}

		float startScale;
		float endScaleX, endScaleY;
		if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds
				.width() / startBounds.height()) {
			// Extend start bounds horizontally
			startScale = (float) startBounds.height() / finalBounds.height();
			endScaleY = (float) finalBounds.height() / intrinsicHeight;
			endScaleX = (float) finalBounds.width() / intrinsicWidth;
			float startWidth = startScale * finalBounds.width();
			float deltaWidth = (startWidth - startBounds.width()) / 2;
			startBounds.left -= deltaWidth;
			startBounds.right += deltaWidth;
		} else {
			// Extend start bounds vertically
			startScale = (float) startBounds.width() / finalBounds.width();
			endScaleX = (float) finalBounds.width() / intrinsicWidth;
			endScaleY = (float) finalBounds.height() / intrinsicHeight;
			float startHeight = startScale * finalBounds.height();
			float deltaHeight = (startHeight - startBounds.height()) / 2;
			startBounds.top -= deltaHeight;
			startBounds.bottom += deltaHeight;
		}

		int mShortAnimationDuration = getResources().getInteger(
				android.R.integer.config_shortAnimTime);
		mShortAnimationDuration *= mAnimSpeed;
		AnimatorSet set = new AnimatorSet();
		/*values modified by kasthuri*/
		set.play(
				ObjectAnimator.ofFloat(relLayout, View.SCALE_X, startScale,
						endScaleX * 0.50f)).with(
				ObjectAnimator.ofFloat(relLayout, View.SCALE_Y, startScale,
						endScaleY * 0.50f));
		gridView.setAlpha(0.0f);//added by kasthuri to blur background
		Log.d(TAG, "Scales are " + startScale + "  end scale x" + endScaleX
				+ "  end scale y" + endScaleY);
		set.setDuration(mShortAnimationDuration);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {

				mAnimated = true;
				SystemClock.sleep(3000);//added by kasthuri to keep the animated image on screen for few seconds
				relLayout.setVisibility(View.GONE);
				gridView.setAlpha(1.0f);//added by kasthuri to undo the blur
				invalidateOptionsMenu();
				final	FrameLayout frmLayout = (FrameLayout) findViewById(R.id.container);
				frmLayout.removeView(relLayout);
				switch (mSelectionMode) {
					case AppConstant.SEL_CROSS_LINE: {
						animateHorizatalLine();
						break;
					}
					case AppConstant.SEL_HIGHLIGHT: {
						initPictureScanData();
					}
					break;
					default:
						break;
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				relLayout.setVisibility(View.GONE);
				mCurrentAnimator = null;
			}
		});
		set.start();
		mCurrentAnimator = set;
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mediaPlayer.release();
		}
		mediaPlayer = null;

		if (mMediaPlayer2 != null) {
			if (mMediaPlayer2.isPlaying()) {
				mMediaPlayer2.stop();
			}
			mMediaPlayer2.release();
		}
		mMediaPlayer2 = null;
		mAudioMgr.abandonAudioFocus(focusListener);
		clearHandler();
		// [START] to fix defect 86
		HomeActivity.setmReturn_CategoryItemsActivityboolVal(true);
		// [END] to fix defect 86



	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
		wl.release();
		clearHandler();

	}

	public void clearHandler() {
		if (mTimeHandler != null) {
			mTimeHandler.removeCallbacks(mTimeRunnable);
			mTimeHandler.removeCallbacks(mScanningRunnable);
			mTimeHandler.removeCallbacks(mEraseRunnable);
			mTimeHandler = null;
		}
		if (mSelectionMode == AppConstant.SEL_CROSS_LINE) {
			Horizatal_line.setVisibility(View.GONE);
			Verticlel_line.setVisibility(View.GONE);
			if (handlerHor != null) {
				handlerHor.removeCallbacks(mhorizatalLineRunnable);
				handlerHor = null;
			}

			if (handlerVer != null) {
				handlerVer.removeCallbacks(mhoVerticleLineRunnable);
				handlerVer = null;
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TAG, "onStop");
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mediaPlayer.release();
		}
		mediaPlayer = null;
		mAudioMgr.abandonAudioFocus(focusListener);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
									ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);
		menu.setHeaderTitle("Options");
		AdapterContextMenuInfo cmi = (AdapterContextMenuInfo) menuInfo;
		Log.i(TAG, ">>>>>>>" + cmi.position);
//		Log.i(TAG, ">>>>>>>" + gridMaxNum);
		mSelected_ID = categoryDataList.get(cmi.position).getID();
		if (cmi.position < gridMaxNum) {
			menu.setHeaderTitle("Options");
		/* [START]::Added code to Position issue for Label: Lavanya H M */
			// level2ItemPosition = cmi.position;
			if (mSelectionMode == AppConstant.SEL_CLICK) {
				Editor editor = sharedpreferences.edit();
				editor.putInt(username, cmi.position);
				editor.commit();
			}
		/* [END]::Added code to Position issue for Label: Lavanya H M */
			Log.i(TAG, ">>>>>>>mSelected_ID:" + mSelected_ID);
			menu.add(AppConstant.GROUP_ID_REPLACEPICTURE, cmi.position, 0,
					getString(R.string.replacePicture));
			menu.add(AppConstant.GROUP_ID_REPLACEVOICE, cmi.position, 1,
					getString(R.string.replaceVoice));
			menu.add(AppConstant.GROUP_ID_REPLACELABEL, cmi.position, 2,
					getString(R.string.replaceLabel));
			menu.add(AppConstant.GROUP_ID_RESET, cmi.position, 3,
					getString(R.string.resetItem));
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch (item.getGroupId()) {

			case AppConstant.GROUP_ID_REPLACEPICTURE:
				Intent photoPickerIntent = new Intent(new Intent(Intent.ACTION_OPEN_DOCUMENT));
				//Intent photoPickerIntent = new Intent(new Intent(Intent.ACTION_PICK,
				//android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
				photoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent,
						AppConstant.PICKPHOTO_RESULT_CODE);
				break;

			case AppConstant.GROUP_ID_REPLACEVOICE:
				if(mSelectedLanguage.equals(AppConstant.DEFAULT_LANGUAGE_CODE)) {
					if(mLevel.equalsIgnoreCase("1")) {
						replace_audio[mSelected_ID] = Boolean.TRUE;
						storeArray(replace_audio, boolean_array_en_lev1, mContext);
					}else{
						replace_audio1[mSelected_ID] = Boolean.TRUE;
						storeArray(replace_audio1, boolean_array_en_lev2, mContext);
					}
				}else if(mSelectedLanguage.equals(AppConstant.OPTIONAL2_LANGUAGE_CODE)){
					if(mLevel.equalsIgnoreCase("1")) {
						replace_audio2[mSelected_ID] = Boolean.TRUE;
						storeArray(replace_audio2, boolean_array1_hi_lev1, mContext);
					}else{
						replace_audio3[mSelected_ID] = Boolean.TRUE;
						storeArray(replace_audio3, boolean_array1_hi_lev2, mContext);
					}
				}
				Intent audioValidateIntent = new Intent(mContext,
						AudioValidate.class);
				startActivityForResult(audioValidateIntent,
						AppConstant.PICKAUDIO_RESULT_CODE);
				break;

			case AppConstant.GROUP_ID_REPLACELABEL:
				if(mSelectedLanguage.equals(AppConstant.DEFAULT_LANGUAGE_CODE)){
					if(mLevel.equalsIgnoreCase("1")) {
						//		Toast.makeText(getApplicationContext(), "replace label if ", Toast.LENGTH_SHORT).show();
						replace_audio[mSelected_ID] = Boolean.FALSE;
						storeArray(replace_audio, boolean_array_en_lev1, mContext);
					}else{
						//	Toast.makeText(getApplicationContext(), "replace label else ", Toast.LENGTH_SHORT).show();
						replace_audio1[mSelected_ID] = Boolean.FALSE;
						storeArray(replace_audio1, boolean_array_en_lev2, mContext);
					}
				}else if(mSelectedLanguage.equals(AppConstant.OPTIONAL2_LANGUAGE_CODE)) {
					if(mLevel.equalsIgnoreCase("1")) {
						replace_audio2[mSelected_ID] = Boolean.FALSE;
						storeArray(replace_audio2, boolean_array1_hi_lev1, mContext);
					}else{
						replace_audio3[mSelected_ID] = Boolean.FALSE;
						storeArray(replace_audio3, boolean_array1_hi_lev2, mContext);
					}
				}
				AlertDialog.Builder editalert = new AlertDialog.Builder(this);
				editalert.setTitle("Label");
				final EditText input = new EditText(this);
				input.setInputType(InputType.TYPE_CLASS_TEXT);
				input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						15) });
				input.setSingleLine();
				input.setLines(1);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.FILL_PARENT);
				input.setLayoutParams(lp);

				editalert.setView(input);

				editalert.setPositiveButton("Set Label",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int whichButton) {
								label = input.getText().toString();
								System.out.println("Label:::" + label + ">>>");
								if (!(label.trim().equalsIgnoreCase(""))) {

									frontController.updateLabelData(
											mSelectedLanguage, mLevel,
											mSelected_ID, label);
									categoryDataList = frontController
											.fetchCategoryData(mSelectedLanguage,
													mLevel, mCategoryId);
									imageAdapter.notifyDataSetChanged();
									//
								} else {
									showAlertOk(CategoryItemsActivity.this,
											"Error", "The label cannot be empty");
								}
							}
						});
				editalert.show();

				break;

			case AppConstant.GROUP_ID_RESET:
				frontController.resetItem(mLevel, mSelectedLanguage, mSelected_ID);
				categoryDataList = frontController.fetchCategoryData(
						mSelectedLanguage, mLevel, mCategoryId);
				imageAdapter.notifyDataSetChanged();

				break;
		}

		return super.onContextItemSelected(item);
	}


	static void showAlertOk(Context context, String title, String message) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(message)
				.setPositiveButton("OK", null).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
									Intent resultData) {
		super.onActivityResult(requestCode, resultCode, resultData);

		switch (requestCode) {

			case AppConstant.PICKPHOTO_RESULT_CODE:
				if (resultCode == RESULT_OK) {
					Uri selectedImage = resultData.getData();
					String selimgpath = selectedImage.getPath();
					Bitmap bitmap = null;

					if (selectedImage.getScheme().toString().compareTo("content") == 0) {
						selimgpath = getPaths(mContext, selectedImage);
						File file = new File(selimgpath);
						if (file.exists() != true) {
							selimgpath = null;

						} else {
							if(selimgpath.contains("KAVIPRO_")) {
								Log.i(TAG, "inside file KAVIPRO_ " + selimgpath);
								replace_picture = true;
								try {
						//			Toast.makeText(getApplicationContext(), selimgpath, Toast.LENGTH_SHORT).show();
									FileInputStream fis = new FileInputStream(selimgpath);
									// Save the decrypted image
									String[] separated = selimgpath.split("/");
								//	Toast.makeText(getApplicationContext(), "seperated is" + selimgpath, Toast.LENGTH_SHORT).show();
									int split = separated.length;
									temp = "decrypt" + separated[split-1];
									saveFile(decrypt(key, fis), temp);

								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								selimgpath = getFilesDir() + "/" + temp;
								//Toast.makeText(CategoryItemsActivity.this, "after ecrypt", Toast.LENGTH_LONG).show();
								//Toast.makeText(getApplicationContext(), selimgpath, Toast.LENGTH_SHORT).show();
								bitmap = DecodeImageUri.decodeSampledBitmapFromAssets(
										mContext, selimgpath, 100, 100);
							}else{
								bitmap = DecodeImageUri.decodeSampledBitmapFromAssets(
										mContext, selimgpath, 100, 100);
							}
						}
					} else {
						selimgpath = selectedImage.getPath();
						try {
							bitmap = MediaStore.Images.Media.getBitmap(
									getContentResolver(), selectedImage);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					if (bitmap != null) {
						bitmap.recycle();
						if (selectedImage.getScheme().toString()
								.compareTo("content") == 0) {
							selimgpath = getPaths(mContext, selectedImage);
							File file = new File(selimgpath);
							if (file.exists() != true) {
								selimgpath = null;

							}
						} else
							selimgpath = selectedImage.getPath();

						Log.i(TAG, "Image URI = " + selectedImage.toString());
						Log.i(TAG, "Image path   = " + selimgpath);
						if (selimgpath != null) {
							if(replace_picture == true) {
								replace_picture = false;
								selimgpath = getFilesDir() + "/" + temp;
							}else{

							}
							frontController.updatePictureData(mSelectedLanguage,
									mLevel, mSelected_ID, selimgpath);
							categoryDataList = frontController.fetchCategoryData(
									mSelectedLanguage, mLevel, mCategoryId);
							updateUI();
							imageAdapter.notifyDataSetChanged();
						} else {
							showAlertOk(mContext, "Error:",
									"Cannot replace the image. The file is either corrupt or missing");
						}
					} else {
						showAlertOk(mContext, "Error:",
								"Cannot replace the image. The file is either corrupt or missing");
					}

				}
				break;


			case AppConstant.PICKAUDIO_RESULT_CODE:

				if (resultCode == RESULT_OK) {
					String audioFileStr;
					Uri audioFile = resultData.getData();
					if (audioFile.getScheme().toString().compareTo("content") == 0) {
						audioFileStr = getRealPathFromURI(audioFile);
					} else
						audioFileStr = audioFile.getPath();

					Log.i(TAG, "Audio URI = " + audioFile);
					Log.i(TAG, "Audio filename string = " + audioFileStr);
					if (audioFileStr != null) {
					//	Toast.makeText(getApplication(), "updating audio file", Toast.LENGTH_LONG).show();
						frontController.updateAudioData(mSelectedLanguage, mLevel,
								mSelected_ID, audioFileStr, mGender);
						categoryDataList = frontController.fetchCategoryData(
								mSelectedLanguage, mLevel, mCategoryId);
						imageAdapter.notifyDataSetChanged();
					} else {
						showAlertOk(mContext, "Error:",
								"Cannot replace the Audio. The file is either corrupt or missing");
					}
				}
				break;
		}

	}

	private byte[] decrypt(byte[] keyValue, FileInputStream fis){

		SecretKeySpec skeySpec = new SecretKeySpec(keyValue, "AES");
		Cipher cipher;
		byte[] decryptedData=null;
		CipherInputStream cis=null;
		int bytesRead;

		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(iv));
			// Create CipherInputStream to read and decrypt the image data
			cis = new CipherInputStream(fis, cipher);                // Write encrypted image data to ByteArrayOutputStream
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			byte[] data = new byte[1024];
			while ((bytesRead = cis.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, bytesRead);
			}
			buffer.flush();
			//byte[] resdata = buffer.toByteArray();
			decryptedData=buffer.toByteArray();
			//String newres = new String(resdata, "UTF-8").trim();

		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try {
				fis.close();
				cis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return decryptedData;
	}

	public void saveFile(byte[] data, String outFileName){
		FileOutputStream fos=null;
		try {
			//fos=new FileOutputStream(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS)+ File.separator+outFileName);
			fos = new FileOutputStream(getFilesDir()+File.separator+outFileName);
			//	File path=new File(getFilesDir(),"myfolder");
			//File mypath=new File(path,outFileName);
			//	new BufferedWriter(new FileWriter(mypath));
			fos.write(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		finally{
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static String getPaths(final Context context, final Uri uri) {
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {
				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				final String selection = "_id=?";
				final String[] selectionArgs = new String[]{split[1]};
				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {column};
		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, null,
				null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}


	// [START] of fix to defect 71p
	public void adjustGridView() {
		mPictureScanHilight = false;
		if (mLevel.equalsIgnoreCase("2"))
			mColumnsCount = Integer
					.parseInt(sharedpreferences.getString(
							"colPref",
							getResources().getString(
									R.string.defValueOfColumn_level2)));
		else
			mColumnsCount = Integer.parseInt(getResources().getString(
					R.string.defValueOfColumn));

		if (mTimeHandler != null) {
			mTimeHandler.removeCallbacks(mTimeRunnable);
			mTimeHandler.removeCallbacks(mScanningRunnable);
			mTimeHandler.removeCallbacks(mEraseRunnable);
			mTimeHandler = null;
		}
		if (handlerHor != null) {
			handlerHor.removeCallbacks(mhorizatalLineRunnable);
			handlerHor = null;
		}

		if (handlerVer != null) {
			handlerVer.removeCallbacks(mhoVerticleLineRunnable);
			handlerVer = null;
		}
		updateUI();
	}// [END] of fix to defect 71

	void showAlert(Context context, String title, String message) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(message)
				.setPositiveButton("OK", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent launchIntent = new Intent(mContext,
								CategoryItemsActivity.class);
						launchIntent.putExtra(AppConstant.CATEGORY_ID,
								mCategoryId);
						launchIntent.putExtra(AppConstant.LEVEL, mLevel);
						launchIntent.putExtra("user",username);//modified by kasthuri included additional argument
						launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						mContext.startActivity(launchIntent);

					}
				}).show().setCancelable(false);
	}

	void hideNavigation() {
		runOnUiThread(new Runnable() {
			public void run() {
				if (Build.VERSION.SDK_INT > 13) { // from version 4.0

					if (Build.VERSION.SDK_INT < 19) { //19 or above api
						View v = getWindow().getDecorView();
						v.setSystemUiVisibility(View.INVISIBLE);
					} else {
						//for lower api versions.
						View decorView = getWindow().getDecorView();
						int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
						decorView.setSystemUiVisibility(uiOptions);
					}
				}
			}
		});
	}
}