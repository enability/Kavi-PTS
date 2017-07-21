package com.enability.kavipts.view;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Locale;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData.Item;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.text.InputFilter;
import android.text.Layout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.provider.Settings;

import android.widget.Toast;

import com.enability.kavipts.AppConstant;
import com.enability.kavipts.R;
import com.enability.kavipts.controller.FrontController;
import com.enability.kavipts.model.ItemData;
import com.enability.kavipts.persistence.CreateDatabaseAdapter;
import com.enability.kavipts.utilities.ConfigKavi;
import com.enability.kavipts.utilities.CreateFolder;
import com.enability.kavipts.utilities.DecodeImageUri;
import com.enability.kavipts.utilities.MD5CheckSum;

import org.apache.http.params.CoreConnectionPNames;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.os.Environment.getExternalStorageState;

public class HomeActivity extends Activity {

	PowerManager pm;
	WakeLock wl;
	String ACTIVATE_LAUNCH = "com.launchb"; //added by kasthuri
	private SharedPreferences sharedpreferences;
	public static final String TAG = "HomeActivity-KAVIPTS";
	private FrontController frontController;
	private ArrayList<ItemData> highLevelDataList;
	private String mLevel = "1";
	private String mMode = "";
	private int mdisplay_mode;
	private MediaPlayer mediaPlayer, mMediaPlayer2;
	private GridView gridView;
	private ImageAdapter imageAdapter;
	private Context mContext;
	private int mColumnsCount = 2;
	private int mSelected_ID = 0;
	protected String mLabel = "";
	private Animator mCurrentAnimator;
	private LinearLayout relLayout;
	private int mAnimSpeed;
	private boolean mAnimated = true, mVoiced = true;
	private AudioManager mAudioMgr;
	AudioManager.OnAudioFocusChangeListener focusListener;
	private Bitmap mAnimBitmap = null;
	int mColorposition = 0;
	public static final String MyPREFERENCES = "MyPrefs";
	//SharedPreferences sharedpreferences;
	int mGender = AppConstant.GENDER_MALE;
	int mSelectionMode = AppConstant.SEL_CLICK;
	private Handler mTimeHandler = null;
	private Runnable mTimeRunnable, mScanningRunnable, mEraseRunnable;
	private int mPictureScanIndex = 0, mMinPos = 0, mMaxPos = 0,
			gridMaxNum = 0;
	View mThumbView = null;
	private Boolean mPictureScanHilight = false;
	private AdapterView.OnItemClickListener mOnItemClick = null;
	private View Horizatal_line;
	private View Verticlel_line;
	private float mhorizatal_y;
	private float mVerticle_x;
	private float mDelta = 1;
	private boolean onclikhorizatalLine = false;
	private boolean onclikVerticleLine = false;
	private float mSelected_y;
	FrameLayout frameLayout;
	protected float selected_x;
	private int mGridscanPos = 0;
	private int mGridscanDelta = 0;
	private int mScanSpeed = 40;
	private String mDefaultspeed = "40";
	private Handler handlerHor = null;
	private Handler handlerVer = null;
	private Runnable mHorizatalLineRunnable, mVerticleLineRunnable;
	/* [END]::Added code cross hair ScanLine: Lavanya H M */
	private int mPictureHighlightSpeed = 3000;
	protected boolean mLimitExced = false;
	private Runnable mRedrawRunnable;
	private android.view.ViewGroup.LayoutParams layoutParamsHor;
	private android.view.ViewGroup.LayoutParams layoutParamsVer;
	private int mScanLinethickness;
	private Button btn_confirm;
	private String mSelectedLanguage = "en";
	private Locale mLocale;
	protected CreateDatabaseAdapter createDatabaseAdapter;
	public static boolean mReturn_CategoryItemsActivityboolVal;
	public String mFileChecksum;
	public static boolean mScanItemboolVal;
	public static boolean mReturn_PrefsActivtyboolVal;
	int scrollIndex;
	String hexColor;
	String username;
	String filename_date;
	String User;
	File path = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS);
	//File file = new File(path, "Kavi.log");
	String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
	int screenSize;
	String tempbgcolor;

	byte[] key={'e','n','a','b','i','l','i','t','y','t','w','e','n','t','y','f','o','u','r','f','o','u','n','d','a','t','i','o','n','I','I','T'};
	byte[] iv = {'a','S','S','i','S','t','i','v','e','L','A','B','I','I','T','M'};
	String temp;
	private static boolean replace_picture;
	private static boolean imgrep;
	int i;
	String msg="";
	TextToSpeech ttobj;
	String[] bob = { "Basic Needs", "Action", "Common Objects", "Vegetables", "Fruits", "Body Parts", "Drinks", "Food", "Places", "Dresses"};
	String[] bob2 = {"मौलिक आवश्यकताएं", "कार्य", "आम वस्तुएं", "सब्जियां", "फल", "शरीर के अंग", "पीना", "आहार", "स्थानों", "कपड़े"};
	//int[] replace_audio={0};
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
	int amStreamMusicMaxVol;
	int amStreamVoiceCall;
	int amStreamCommunication;
	int x;
	HashMap<String, String> myHashAlarmVoiceCall = new HashMap();
	HashMap<String, String> myHashAlarmStreamMusic = new HashMap();
	HashMap<String, String> myHashAlarmStreamSystem = new HashMap();

	//String temp;
	//byte[] key, iv;
	public static boolean ismScanItemboolVal() {
		return mScanItemboolVal;
	}

	public static boolean ismReturn_PrefsActivtyboolVal() {
		return mReturn_PrefsActivtyboolVal;
	}

	public static void setmReturn_PrefsActivtyboolVal(
			boolean mReturn_PrefsActivtyboolVal) {
		HomeActivity.mReturn_PrefsActivtyboolVal = mReturn_PrefsActivtyboolVal;
	}

	public static void setmScanItemboolVal(boolean mScanItemboolVal) {
		HomeActivity.mScanItemboolVal = mScanItemboolVal;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.singlecolumn_gridlayout);
		createSpeechObject();
		initializeAudio();
		hideNavigation();
		mContext = this;
		highLevelDataList = new ArrayList<ItemData>();
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
		Intent intent = getIntent();
		username = intent.getExtras().getString("user");
		//Toast.makeText(getApplicationContext(),username,Toast.LENGTH_SHORT).show();
		mAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		createDatabaseAdapter = new CreateDatabaseAdapter(mContext, username); //modified by kasthuri included additional argument
		filename_date = username + "_" + date + ".log";
		frontController = FrontController.getInstance(this);
		frontController.initialize();
//added by kasthuri
		 screenSize = getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		switch (screenSize) {
			case Configuration.SCREENLAYOUT_SIZE_XLARGE:
			//	Toast.makeText(getApplicationContext(), "Your screensize is " + screenSize,  Toast.LENGTH_SHORT).show();
				break;
			case Configuration.SCREENLAYOUT_SIZE_LARGE:
			//	Toast.makeText(getApplicationContext(), "Your screensize is " + screenSize,  Toast.LENGTH_SHORT).show();
				break;
			case Configuration.SCREENLAYOUT_SIZE_NORMAL:
			//	Toast.makeText(getApplicationContext(), "Your screensize is " + screenSize,  Toast.LENGTH_SHORT).show();
				break;
			case Configuration.SCREENLAYOUT_SIZE_SMALL:
			//	Toast.makeText(this, "Small screen", Toast.LENGTH_LONG).show();
				break;
			default:
			//	Toast.makeText(this,
				//		"Screen size is neither large, normal or small",
				//		Toast.LENGTH_LONG).show();
		}
		copyAssests();
		//key=getKey();

		// Get IV


	//	iv=getIV();
		replace_audio = loadArray(boolean_array_en_lev1, mContext);
		replace_audio1 = loadArray(boolean_array_en_lev2, mContext);
		replace_audio2 = loadArray(boolean_array1_hi_lev1, mContext);
		replace_audio3 = loadArray(boolean_array1_hi_lev2, mContext);
	/*	if((mLevel.equalsIgnoreCase("1"))&&(mSelectedLanguage.equals(AppConstant.DEFAULT_LANGUAGE_CODE))) {

		}else if((mLevel.equalsIgnoreCase("2"))&&(mSelectedLanguage.equals(AppConstant.DEFAULT_LANGUAGE_CODE))) {

		}else if((mLevel.equalsIgnoreCase("1"))&&(mSelectedLanguage.equals(AppConstant.OPTIONAL2_LANGUAGE_CODE))){

		}else if((mLevel.equalsIgnoreCase("2"))&&(mSelectedLanguage.equals(AppConstant.OPTIONAL2_LANGUAGE_CODE))) {

		}*/
		//Default sharedpreferences is changed to sharedprefrences with respect to the username edited by kasthuri to save user settings
		sharedpreferences = getSharedPreferences(username, MODE_PRIVATE);

		mSelectionMode = Integer.parseInt(sharedpreferences.getString("selection_mode", "1"));
		if (mSelectionMode == AppConstant.SEL_CROSS_LINE) {
			initScaningLine();
		}
		mSelectedLanguage = sharedpreferences.getString("selection_language", "en");
		//
		Log.d(TAG, "-------------------- creating folders-----------------"
				+ CreateFolder.create(mContext));
		//

	}

	private static byte[]  getKey(){
		KeyGenerator keyGen;
		byte[] dataKey=null;

		try {

		// Generate 256-bit key
			keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(256);
			SecretKey secretKey = keyGen.generateKey();
			dataKey=secretKey.getEncoded();

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataKey;
	}


	@Override
	protected void onStart() {
		super.onStart();
			mLevel = sharedpreferences.getString("level", getResources().getString(R.string.defLevel));
			highLevelDataList = frontController.fetchHighLevelData(
					mSelectedLanguage, mLevel);
			//	getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
			PreferenceManager.setDefaultValues(mContext, R.xml.prefs, false);

	}

	@SuppressLint("NewApi")
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, "onRestart  >>>>>>*****");
		gridView.setSelection(scrollIndex);

		mAnimSpeed = Integer.parseInt(sharedpreferences.getString("animspeed", "4"));
		mMode = sharedpreferences.getString("modes", getResources()
				.getString(R.string.defMode));

		String backColor = sharedpreferences.getString(AppConstant.PREF_BACK_COLOR,
				"#ffFFFF66");

		ColorSingleton.getInstance().setColor(
				sharedpreferences.getString(AppConstant.PREF_BACK_COLOR, "#ffFFFF66"));
		LinearLayout ll = (LinearLayout) findViewById(R.id.parent_view);
		ll.setBackgroundColor(Color.parseColor(backColor));
		mLevel = sharedpreferences.getString("level",
				getResources().getString(R.string.defLevel));
		if (mLevel.equalsIgnoreCase("2")
				&& mdisplay_mode == Configuration.ORIENTATION_LANDSCAPE)
			mColumnsCount = Integer
					.parseInt(sharedpreferences.getString(
							"colPref",
							getResources().getString(
									R.string.defValueOfColumn_level2)));
		else
			mColumnsCount = Integer.parseInt(getResources().getString(
					R.string.defValueOfColumn));
		highLevelDataList = FrontController.getInstance(mContext)
				.fetchHighLevelData(mSelectedLanguage, mLevel);
		mGender = Integer.parseInt(sharedpreferences.getString("gender", "1"));
		Log.d(TAG, "****************** restart GGGGGGGender " + mGender);
		mSelectionMode = Integer.parseInt(sharedpreferences.getString("selection_mode", "1"));
		mSelectedLanguage = sharedpreferences.getString("selection_language", "en");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		mdisplay_mode = getResources().getConfiguration().orientation;
		if (mLevel.equalsIgnoreCase("2")
				&& mdisplay_mode == Configuration.ORIENTATION_LANDSCAPE)
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
			handlerHor.removeCallbacks(mHorizatalLineRunnable);
			handlerHor = null;
		}
		if (handlerVer != null) {
			handlerVer.removeCallbacks(mVerticleLineRunnable);
			handlerVer = null;
		}

		// {START} fix for scan line issue on home screen
	//	initScaningLine();
		mPictureScanHilight = false;

		// {END} fix for scan line issue on home screen
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

	public boolean storeArray1(Boolean[] array, String arrayName, Context mContext) {

		SharedPreferences prefsname = mContext.getSharedPreferences("prefname", 0);
		SharedPreferences.Editor editor = prefsname.edit();
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

	public Boolean[] loadArray1(String arrayName, Context mContext) {

		SharedPreferences prefsname = mContext.getSharedPreferences("prefname", 0);
		int size = prefsname.getInt(arrayName + "_size", 100);
		Boolean array[] = new Boolean[size];
		for(int i=0;i<size;i++)
			array[i] = prefsname.getBoolean(arrayName + "_" + i, false);

		return array;
	}

	public void copyAssests()
	{
		AssetManager assetManager = getAssets();
		String[] files = null;
		try {
			files = assetManager.list("");
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String filename : files)
		{
			System.out.println("In CopyAssets"+filename);
			InputStream in = null;
			OutputStream out = null;
			try {
				in = assetManager.open(filename);
				String foldername= Environment.getExternalStorageDirectory().getPath()+"/Android/data/"+getPackageName().toString()+"/";
				File folder = new File(foldername);
				folder.mkdirs();
				File outfile = new File(foldername+filename);
				out = new FileOutputStream(outfile);
				copyFile(in, out);
				System.out.println("In copyAssets Entire Path"+foldername+filename);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while((read = in.read(buffer)) != -1){
			out.write(buffer, 0, read);
		}
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
							//Toast.makeText(getApplicationContext(), "Your selected language is " + mSelectedLanguage,  Toast.LENGTH_SHORT).show();
							//if(ttobj.isLanguageAvailable(new Locale("hi"))==TextToSpeech.LANG_AVAILABLE)
							ttobj.setLanguage(Locale.US);
						//	ttobj.setLanguage(new Locale("hin-IND"));
						} else if (status == TextToSpeech.ERROR) {
							Toast.makeText(getApplication(), "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
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
		Toast.makeText(HomeActivity.this,mainfn(inputtext,filename,wavname),Toast.LENGTH_SHORT).show();
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

	@SuppressLint("NewApi")
	public void initScaningLine() {
		Horizatal_line = (View) findViewById(R.id.horizatal_line);
		Verticlel_line = (View) findViewById(R.id.verticle_line);
		Horizatal_line.setVisibility(View.VISIBLE);
		Verticlel_line.setVisibility(View.VISIBLE);
		Horizatal_line.setY(0);
		Verticlel_line.setX(0);
		String scanLineColor = sharedpreferences.getString(AppConstant.PREF_SCANLINE_COLOR,
				"#000000");
		mScanLinethickness = Integer.parseInt(sharedpreferences.getString(
				AppConstant.PREF_SCANLINE_THICKNESS, "5"));
		layoutParamsHor = Horizatal_line.getLayoutParams();
		layoutParamsHor.height = mScanLinethickness;
		Horizatal_line.setLayoutParams(layoutParamsHor);
		layoutParamsVer = Verticlel_line.getLayoutParams();
		layoutParamsVer.width = mScanLinethickness;
		Verticlel_line.setLayoutParams(layoutParamsVer);
		mScanSpeed = Integer
				.parseInt(sharedpreferences.getString("scan_speed", mDefaultspeed));
		Horizatal_line.setBackgroundColor(Color.parseColor(scanLineColor));
		Verticlel_line.setBackgroundColor(Color.parseColor(scanLineColor));
		mGridscanPos = 0;
		mGridscanDelta = 0;
		onclikhorizatalLine = false;
		onclikVerticleLine = false;
	}
	public void executeApp(String packageName) { //added by kasthuri to go back to launchmenu when pressing back button

		Intent newAppIntent = getPackageManager().getLaunchIntentForPackage(packageName);
		if (newAppIntent != null) {
			newAppIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(newAppIntent);
			finish();
		}
		else
			Toast.makeText(getBaseContext(), "APPLICATION NOT INSTALLED" ,Toast.LENGTH_SHORT).show();
	}
	private void initPictureScanData() {
		mPictureScanIndex = 0;
		//String borderLineColor = sharedpreferences.getString(AppConstant.PREF_BORDER_COLOR,
		//		"#000000");
		mPictureHighlightSpeed = Integer.parseInt(sharedpreferences.getString(
				"picture_highlight_speed", "3000"));
	//	String filename = username + ".log";
		File file = new File(path, filename_date);
		BufferedReader br = null;
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true), 8192);
			writer.write("scan speed" + " " + mPictureHighlightSpeed + " " + time +"\n");  // printing scan speed
			writer.close();
			//Toast.makeText(getBaseContext(), "yes path!!! =)",
			//  Toast.LENGTH_LONG).show();
		}catch (Exception e) {
			//  Toast.makeText(getBaseContext(), "IN EXCEPTION" + e.toString(), Toast.LENGTH_SHORT).show();

		}
		Log.d(TAG, "ScanLine grid max is " + gridMaxNum);

		mTimeHandler = new Handler();
		mEraseRunnable = new Runnable() {

			@Override
			public void run() {
				mPictureScanHilight = false;
				Log.d(TAG, "ScanLine Erased The max pos and min pos are "
						+ mMaxPos + "  " + mMinPos + "  " + mPictureScanIndex);
				View thumb = gridView.getChildAt(mPictureScanIndex - mMinPos);
				try {
					if (thumb != null) {
						thumb.setBackgroundColor(Color
								.parseColor(ColorSingleton.getInstance()
										.getColor()));
						mPictureScanIndex++;
					}

				} catch (Exception e) {
					Log.d(TAG, "thumb value is Null  ");

				}

			}
		};

		mScanningRunnable = new Runnable() {

			@Override
			public void run() {
				mMinPos = gridView.getFirstVisiblePosition();
				mMaxPos = gridView.getLastVisiblePosition();
				Log.d(TAG, "ScanLine The max pos and min pos are " + mMaxPos
						+ "  " + mMinPos + "  " + mPictureScanIndex);
				View thumb = gridView.getChildAt(mPictureScanIndex - mMinPos);

				if (thumb != null) {
					mPictureScanHilight = true;
					if(hexColor != null) { //added by kasthuri to change the scanning border color
						thumb.setBackgroundColor(Color.parseColor(hexColor));
					}else{
						thumb.setBackgroundColor(Color.parseColor("#0f0f0f"));
					}

				}

			}
		};
		mTimeRunnable = new Runnable() {

			@Override
			public void run() {
				try {
					Log.d("logical", "mPictureScanIndex === "
							+ mPictureScanIndex);

					if (mPictureScanIndex <= gridMaxNum) {
						int row = mPictureScanIndex / mColumnsCount;
						int selectionId = ((row + 1) * mColumnsCount) - 1;
						gridView.smoothScrollToPosition(selectionId);
						mTimeHandler.postDelayed(this, mPictureHighlightSpeed);
						// [START] fix to defect 99
						mTimeHandler.postDelayed(mScanningRunnable, 100);

						mTimeHandler.postDelayed(mEraseRunnable, 1800);
						// [END] fix to defect 99
					} else {
						// [START] of fix to defect 71
						adjustGridView();
						// [END] of fix to defect 71
					}

				} catch (Exception e) {

				}

			}
		};

		mTimeHandler.postDelayed(mTimeRunnable, mPictureHighlightSpeed);

	}

	@Override
	protected void onResume() {
		super.onResume();
		sharedpreferences = getSharedPreferences(username,
				Context.MODE_PRIVATE);
	//	SharedPreferences.Editor editor = sharedpreferences.edit();
		wl.acquire();
		//
		if (sharedpreferences.getBoolean("firstrun", true)) {
			mFileChecksum = MD5CheckSum.calculateCheckSum();
			sharedpreferences.edit().putString(AppConstant.PREF_CHECKSUM_VAL, mFileChecksum)
					.commit();

			sharedpreferences.edit().putBoolean("firstrun", false).commit();
		}
		String fileChecksum = MD5CheckSum.calculateCheckSum();
		// for operational mode
		if (fileChecksum != null) {
			if (!fileChecksum.equals(sharedpreferences.getString(
					AppConstant.PREF_CHECKSUM_VAL, "default_val"))) {
				sharedpreferences.edit()
						.putString(AppConstant.PREF_CHECKSUM_VAL, fileChecksum)
						.commit();
				HashMap<String, String> launchpadItems = new HashMap<String, String>();
				launchpadItems = ConfigKavi.parseLaunchPadFile();
				if (launchpadItems.get("Level").equalsIgnoreCase("Advanced")) {
					sharedpreferences.edit()
							.putString("modes",
									getResources().getString(R.string.defMode))
							.commit();
				} else if (launchpadItems.get("Level").equalsIgnoreCase(
						"Beginner")) {
					sharedpreferences.edit().putString("modes", "student").commit();
				}

				// for Gender mode
				if (launchpadItems.get("Gender").equalsIgnoreCase("Male")) {
					sharedpreferences.edit().putString("gender", "1").commit();
				} else if (launchpadItems.get("Gender").equalsIgnoreCase(
						"Female")) {
					sharedpreferences.edit().putString("gender", "2").commit();
				}
				// for speed
				if (launchpadItems.get("Speed").equalsIgnoreCase("Medium")) {
					if (mSelectionMode == AppConstant.SEL_HIGHLIGHT) {
						sharedpreferences.edit().putString("picture_highlight_speed", "3000")
								.commit();
					} else if (mSelectionMode == AppConstant.SEL_CROSS_LINE) {
						sharedpreferences.edit().putString("scan_speed", "40").commit();
					}
				} else if (launchpadItems.get("Speed").equalsIgnoreCase("Fast")) {
					if (mSelectionMode == AppConstant.SEL_HIGHLIGHT) {
						sharedpreferences.edit().putString("picture_highlight_speed", "2000")
								.commit();
					} else if (mSelectionMode == AppConstant.SEL_CROSS_LINE) {
						sharedpreferences.edit().putString("scan_speed", "10").commit();
					}
				} else if (launchpadItems.get("Speed").equalsIgnoreCase("Slow")) {
					if (mSelectionMode == AppConstant.SEL_HIGHLIGHT) {
						sharedpreferences.edit().putString("picture_highlight_speed", "4000")
								.commit();
					} else if (mSelectionMode == AppConstant.SEL_CROSS_LINE) {
						sharedpreferences.edit().putString("scan_speed", "60").commit();
					}
				}
			}
		}

		//
		mediaPlayer = new MediaPlayer();
		mdisplay_mode = getResources().getConfiguration().orientation;
		mPictureScanHilight = false;
		mAnimSpeed = Integer.parseInt(sharedpreferences.getString("animspeed", "4"));
		// [START] fix for defect 74
		mScanSpeed = Integer
				.parseInt(sharedpreferences.getString("scan_speed", mDefaultspeed));
		// [END] fix for defect 74
		mMode = sharedpreferences.getString("modes", getResources()
				.getString(R.string.defMode));
		String backColor = sharedpreferences.getString(AppConstant.PREF_BACK_COLOR,
				"#ffFFFF66");
		LinearLayout ll = (LinearLayout) findViewById(R.id.parent_view);
		ll.setBackgroundColor(Color.parseColor(backColor));
		Log.d(TAG, "mMode:::" + mMode);
		mLevel = sharedpreferences.getString("level",
				getResources().getString(R.string.defLevel));

		// [START] fix for defect 122
		if (ismReturn_PrefsActivtyboolVal()) {
			if (mSelectionMode == AppConstant.SEL_HIGHLIGHT
					&& mLevel.equals("1")) {
		//		String filename = username + ".log";
				File file = new File(path, filename_date);
				BufferedReader br = null;
			 		try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(file, true), 8192);
					String scan = "Scan mode"; //printing mode of navigation
					writer.write(scan + " " + time + "\n");
					writer.close();
					//Toast.makeText(getBaseContext(), "yes path!!! =)",
					//  Toast.LENGTH_LONG).show();
				}catch (Exception e) {
					//  Toast.makeText(getBaseContext(), "IN EXCEPTION" + e.toString(), Toast.LENGTH_SHORT).show();

				}
				//Intent newintent = getIntent();
				//username = newintent.getExtras().getString("user");
				gridView.setOnItemClickListener(null);//added by kasthuri to create logfile with username
			/*	Toast.makeText(getApplicationContext(),username,Toast.LENGTH_SHORT).show();
				Bundle bundle = new Bundle(); //commented by kasthuri (it resets the entire app and couldn't receive username to create the logfile with the username)
				Intent i = new Intent(HomeActivity.this, HomeActivity.class);
				i.putExtra("user",username);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);*/
			}
			setmReturn_PrefsActivtyboolVal(false);
		}
		// [END] fix for defect 122

		if (mLevel.equalsIgnoreCase("2")
				&& mdisplay_mode == Configuration.ORIENTATION_LANDSCAPE)
			mColumnsCount = Integer
					.parseInt(sharedpreferences.getString(
							"colPref",
							getResources().getString(
									R.string.defValueOfColumn_level2)));
		else
			mColumnsCount = Integer.parseInt(getResources().getString(
					R.string.defValueOfColumn));
		mGender = Integer.parseInt(sharedpreferences.getString("gender", "1"));
		Log.d(TAG, "****************** resume GGGGGGGender " + mGender);
		mSelectionMode = Integer.parseInt(sharedpreferences.getString("selection_mode", "1"));
		mSelectedLanguage = sharedpreferences.getString(AppConstant.PREF_SELECTED_LANGUAGE,
				"en");
		if (mSelectionMode == AppConstant.SEL_CROSS_LINE) {
	//		BufferedReader br = null;
		//	String filename = username + ".log";
			File file = new File(path, filename_date);
			BufferedReader br = null;
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file, true), 8192);
				String crossline = "Crossline mode"; //printing mode of navigation
				writer.write(crossline + " " + time + "\n");
				writer.close();
				//Toast.makeText(getBaseContext(), "yes path!!! =)",
				//  Toast.LENGTH_LONG).show();
			}catch (Exception e) {
				//  Toast.makeText(getBaseContext(), "IN EXCEPTION" + e.toString(), Toast.LENGTH_SHORT).show();

			}
			// [START] fix for defect 74
			if (handlerHor != null) {
				handlerHor.removeCallbacks(mHorizatalLineRunnable);
				handlerHor = null;
			}
			if (handlerVer != null) {
				handlerVer.removeCallbacks(mVerticleLineRunnable);
				handlerVer = null;
			}
			// [END] fix for defect 74
			initScaningLine();
		}
		// [START] to fix defect 86
		if ((mSelectionMode == AppConstant.SEL_HIGHLIGHT)
				&& (ismReturn_CategoryItemsActivityboolVal())) {
			if (mLevel.equalsIgnoreCase("2")
					&& mdisplay_mode == Configuration.ORIENTATION_LANDSCAPE)
				mColumnsCount = Integer.parseInt(sharedpreferences.getString(
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

			setmReturn_CategoryItemsActivityboolVal(false);
		}
		//

		initiateLayoutHighLightSelect();

		//
		updateUI();
		// [END] to fix defect 86
		if (mSelectionMode == AppConstant.SEL_CLICK) {
			gridView.setOnItemClickListener(mOnItemClick);
		}

	}

	private void initiateLayoutHighLightSelect() {
		LinearLayout container = (LinearLayout) findViewById(R.id.parent_view);
		container.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (mSelectionMode) {
					case AppConstant.SEL_HIGHLIGHT: {
						if (mPictureScanHilight) {
							View thumb = gridView.getChildAt(mPictureScanIndex
									- mMinPos);
							mOnItemClick.onItemClick(null, thumb,
									mPictureScanIndex, 0);

							if (mTimeHandler != null) {
								mTimeHandler.removeCallbacks(mTimeRunnable);
								mTimeHandler.removeCallbacks(mScanningRunnable);
								mTimeHandler.removeCallbacks(mEraseRunnable);
								mTimeHandler = null;
							}
						}

					}
					break;
					default:
						break;
				}
			}
		});
	}

	private void updateUI() {
		ColorSingleton.getInstance().setColor(
				sharedpreferences.getString(AppConstant.PREF_BACK_COLOR, "#ffFFFF66"));
		tempbgcolor = sharedpreferences.getString(AppConstant.PREF_BACK_COLOR, "#ffFFFF66");
		hexColor =  sharedpreferences.getString( //added ny kasthuri to save the updated  border color
				AppConstant.PREF_BORDER_COLOR, "#ff0f0f0f");
		gridView = (GridView) findViewById(R.id.imagegridView);
		imageAdapter = new ImageAdapter(mContext, username, mSelectedLanguage, highLevelDataList,
				mColumnsCount, 0, screenSize);//modified by kasthuri included additional argument
		gridView.setAdapter(imageAdapter);
		gridView.setNumColumns(mColumnsCount);
//		String filename = username + ".log";
		File file = new File(path, filename_date);
		BufferedReader br = null;//added by kasthuri to create logfile
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true), 8192);
			writer.write("border color is " + hexColor + " " + time + "\n"); //printing levels
			writer.write("background color is " + tempbgcolor + " " + time + "\n");
			writer.close();
			//Toast.makeText(getBaseContext(), "yes path!!! =)",
			//  Toast.LENGTH_LONG).show();
		}catch (Exception e) {
			//  Toast.makeText(getBaseContext(), "IN EXCEPTION" + e.toString(), Toast.LENGTH_SHORT).show();

		}
		gridView.setScrollingCacheEnabled(true);
		if (mLevel.equalsIgnoreCase("2")) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file, true), 8192);
				writer.write("Level 2" + " " + time + "\n"); //printing levels
				writer.close();
			}catch (Exception e) {
				//  Toast.makeText(getBaseContext(), "IN EXCEPTION" + e.toString(), Toast.LENGTH_SHORT).show();

			}
			gridMaxNum = 10;
		} else {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file, true), 8192);
				writer.write("Level 1" + " " + time + "\n");
				writer.close();
				//Toast.makeText(getBaseContext(), "yes path!!! =)",
				//  Toast.LENGTH_LONG).show();
			}catch (Exception e) {
				//  Toast.makeText(getBaseContext(), "IN EXCEPTION" + e.toString(), Toast.LENGTH_SHORT).show();

			}
			gridMaxNum = 4;
		}

		/*
		 * [START]::Added code to Position issue for Label: Lavanya H M if
		 * (mSelectionMode == AppConstant.SEL_CLICK) { level1ItemPosition =
		 * sharedpreferences.getInt(MyPREFERENCES, 0);
		 * gridView.smoothScrollToPosition(level1ItemPosition);
		 * 
		 * }
		 * 
		 * [END]::Added code to Position issue for Label: Lavanya H M
		 */
		// close progress bar on updatePostUi()

		mOnItemClick = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ItemData data = null;
				if (mAnimated == true) {
					invalidateOptionsMenu();
					mAnimated = false;
					mVoiced = false;

					try {
						data = (ItemData) view.getTag();
					} catch (NullPointerException e) {
						showAlert(HomeActivity.this, "Wrong Selection",
								"Please select scanned item");
						return;
					}

					/*
					 * [START]::Added code to Position issue for Label: Lavanya
					 * H M
					 */
					if (mSelectionMode == AppConstant.SEL_CLICK) {
						Editor editor = sharedpreferences.edit();
						editor.putInt(username, position);
						editor.commit();
						/*
						 * [END]::Added code to Position issue for Label:
						 * Lavanya H M
						 */
					}
					final int category_id = data.getCategory_ID();
					if (category_id == -1) {
						clearPrefence();
				//		executeApp(ACTIVATE_LAUNCH);
						Intent newintent = new Intent(HomeActivity.this, MainActivity.class);
						startActivity(newintent);
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
					int audioResult = mAudioMgr.requestAudioFocus(
							focusListener, AudioManager.STREAM_MUSIC,
							AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);

					if (audioResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
						ImageView thumb = (ImageView) findViewById(R.id.grid_item_image);
						String imageuri = data.getImageURI();
						if (imageuri.contains("android.resource")) {

							zoomImageFromThumb(thumb, data.getImageURI(),
									data.getLabel(), category_id);
						} else {
							File imageFile = new File(data.getImageURI());
							if (imageFile.exists()) {
								zoomImageFromThumb(thumb, data.getImageURI(),
										data.getLabel(), category_id);
							} else {
								if (mLevel.equalsIgnoreCase("2")) {

									if (mSelectedLanguage
											.equals(AppConstant.OPTIONAL_LANGUAGE_CODE)) {
										frontController
												.updatePictureData(
														mSelectedLanguage,
														mLevel,
														data.getID(),
														createDatabaseAdapter
																.opt_lang_getPicture_lev2(data
																		.getID() - 1));
										highLevelDataList = frontController
												.fetchHighLevelData(
														mSelectedLanguage,
														mLevel);
										zoomImageFromThumb(
												thumb,
												createDatabaseAdapter
														.opt_lang_getPicture_lev2(data
																.getID() - 1),
												data.getLabel(), category_id);
									} else if(mSelectedLanguage
											.equals(AppConstant.OPTIONAL1_LANGUAGE_CODE)){
										frontController
												.updatePictureData(
														mSelectedLanguage,
														mLevel,
														data.getID(),
														createDatabaseAdapter
																.opt_lang_getPicture_lev2(data
																		.getID() - 1));
										highLevelDataList = frontController
												.fetchHighLevelData(
														mSelectedLanguage,
														mLevel);
										zoomImageFromThumb(
												thumb,
												createDatabaseAdapter
														.opt_lang_getPicture_lev2(data
																.getID() - 1),
												data.getLabel(), category_id);

									}else if(mSelectedLanguage
											.equals(AppConstant.OPTIONAL2_LANGUAGE_CODE)){
										frontController
												.updatePictureData(
														mSelectedLanguage,
														mLevel,
														data.getID(),
														createDatabaseAdapter
																.opt_lang_getPicture_lev2(data
																		.getID() - 1));
										highLevelDataList = frontController
												.fetchHighLevelData(
														mSelectedLanguage,
														mLevel);
										zoomImageFromThumb(
												thumb,
												createDatabaseAdapter
														.opt_lang_getPicture_lev2(data
																.getID() - 1),
												data.getLabel(), category_id);

									}else if(mSelectedLanguage
											.equals(AppConstant.OPTIONAL3_LANGUAGE_CODE)){
										frontController
												.updatePictureData(
														mSelectedLanguage,
														mLevel,
														data.getID(),
														createDatabaseAdapter
																.opt_lang3_getPicture_lev2(data
																		.getID() - 1));
										highLevelDataList = frontController
												.fetchHighLevelData(
														mSelectedLanguage,
														mLevel);
										zoomImageFromThumb(
												thumb,
												createDatabaseAdapter
														.opt_lang3_getPicture_lev2(data
																.getID() - 1),
												data.getLabel(), category_id);

									}
									else {
										frontController.updatePictureData(
												mSelectedLanguage, mLevel, data
														.getID(),
												createDatabaseAdapter
														.getPicture_lev2(data
																.getID() - 1));
										highLevelDataList = frontController
												.fetchHighLevelData(
														mSelectedLanguage,
														mLevel);
										zoomImageFromThumb(thumb,
												createDatabaseAdapter
														.getPicture_lev2(data
																.getID() - 1),
												data.getLabel(), category_id);
									}
								} else if (mLevel.equalsIgnoreCase("1")) {

									if (mSelectedLanguage
											.equals(AppConstant.OPTIONAL_LANGUAGE_CODE)) {
										frontController
												.updatePictureData(
														mSelectedLanguage,
														mLevel,
														data.getID(),
														createDatabaseAdapter
																.opt_lang_getPicture_lev1(data
																		.getID() - 1));
										highLevelDataList = frontController
												.fetchHighLevelData(
														mSelectedLanguage,
														mLevel);
										zoomImageFromThumb(
												thumb,
												createDatabaseAdapter
														.opt_lang_getPicture_lev1(data
																.getID() - 1),
												data.getLabel(), category_id);
									}else if(mSelectedLanguage
											.equals(AppConstant.OPTIONAL1_LANGUAGE_CODE)){
										frontController
												.updatePictureData(
														mSelectedLanguage,
														mLevel,
														data.getID(),
														createDatabaseAdapter
																.opt_lang_getPicture_lev1(data
																		.getID() - 1));
										highLevelDataList = frontController
												.fetchHighLevelData(
														mSelectedLanguage,
														mLevel);
										zoomImageFromThumb(
												thumb,
												createDatabaseAdapter
														.opt_lang_getPicture_lev1(data
																.getID() - 1),
												data.getLabel(), category_id);
									}else if(mSelectedLanguage
											.equals(AppConstant.OPTIONAL2_LANGUAGE_CODE)){
										frontController
												.updatePictureData(
														mSelectedLanguage,
														mLevel,
														data.getID(),
														createDatabaseAdapter
																.opt_lang_getPicture_lev1(data
																		.getID() - 1));
										highLevelDataList = frontController
												.fetchHighLevelData(
														mSelectedLanguage,
														mLevel);
										zoomImageFromThumb(
												thumb,
												createDatabaseAdapter
														.opt_lang_getPicture_lev1(data
																.getID() - 1),
												data.getLabel(), category_id);
									}else if(mSelectedLanguage
											.equals(AppConstant.OPTIONAL3_LANGUAGE_CODE)){
										frontController
												.updatePictureData(
														mSelectedLanguage,
														mLevel,
														data.getID(),
														createDatabaseAdapter
																.opt_lang_getPicture_lev1(data
																		.getID() - 1));
										highLevelDataList = frontController
												.fetchHighLevelData(
														mSelectedLanguage,
														mLevel);
										zoomImageFromThumb(
												thumb,
												createDatabaseAdapter
														.opt_lang_getPicture_lev1(data
																.getID() - 1),
												data.getLabel(), category_id);
									}
									else {
										Log.i(TAG,
												" LavSe createDatabaseAdapter.getPicture_lev1(data.getID()-1 "
														+ createDatabaseAdapter
																.getPicture_lev1(data
																		.getID() - 1));
										frontController.updatePictureData(
												mSelectedLanguage, mLevel, data
														.getID(),
												createDatabaseAdapter
														.getPicture_lev1(data
																.getID() - 1));
										highLevelDataList = frontController
												.fetchHighLevelData(
														mSelectedLanguage,
														mLevel);
										zoomImageFromThumb(thumb,
												createDatabaseAdapter
														.getPicture_lev1(data
																.getID() - 1),
												data.getLabel(), category_id);

									}
								}
							}
						}

						mediaPlayer = null;
						mediaPlayer = new MediaPlayer();
						Log.i(TAG,
								"The uri is "
										+ Uri.parse(data.getAudioPath(mGender)));
						boolean exceptionOccured = false;
						try {
							String audioPath = data.getAudioPath(mGender);
							if (audioPath.contains("android.resource")) {
								mediaPlayer.setDataSource(mContext,
										Uri.parse(data.getAudioPath(mGender)));
							} else {
								File audioFile = new File(audioPath);
								if (audioFile.exists()) {
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
										}else if(mSelectedLanguage
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
											//	Toast.makeText(getApplication(), msg, Toast.LENGTH_LONG).show();
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
											//	Toast.makeText(getApplication(), msg, Toast.LENGTH_LONG).show();
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
										}else if(mSelectedLanguage
												.equals(AppConstant.OPTIONAL1_LANGUAGE_CODE)){
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
											//	Toast.makeText(getApplication(), msg, Toast.LENGTH_LONG).show();
												synthesisWavInBackground(msg);
											}
										}else if(mSelectedLanguage
												.equals(AppConstant.OPTIONAL3_LANGUAGE_CODE)){
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
											//	Toast.makeText(getApplication(), msg, Toast.LENGTH_LONG).show();
												speakText(msg);
											}

										}
									}
									try {

										AssetFileDescriptor descriptor = getApplicationContext()
												.getAssets().openFd(audioUri);
										exceptionOccured = false;
										long start = descriptor
												.getStartOffset();
										long end = descriptor.getLength();
										mediaPlayer.setDataSource(
												descriptor.getFileDescriptor(),
												start, end);
										mediaPlayer.prepare();
									} catch (Exception e) {
									}
								}
							}
						} catch (IllegalArgumentException e) {
							Log.i(TAG, " the message is " + e.getMessage());
							exceptionOccured = true;
							e.printStackTrace();
						} catch (IllegalStateException e) {
							Log.i(TAG, " the message is " + e.getMessage());
							e.printStackTrace();
							exceptionOccured = true;
						} catch (IOException e) {
							Log.i(TAG, " the message is " + e.getMessage());
							e.printStackTrace();
							exceptionOccured = true;
						}
						if (exceptionOccured == true) {
							String audioUri = null;
							if (mLevel.equalsIgnoreCase("2")) {
								if (mSelectedLanguage
										.equals(AppConstant.OPTIONAL_LANGUAGE_CODE)) {
									audioUri = createDatabaseAdapter
											.opt_lang_getAudio_lev2(
													data.getID() - 1, mGender);

								}else if(mSelectedLanguage
										.equals(AppConstant.OPTIONAL1_LANGUAGE_CODE)){
									audioUri = createDatabaseAdapter
											.opt_lang_getAudio_lev2(
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

								}else if(mSelectedLanguage
										.equals(AppConstant.OPTIONAL1_LANGUAGE_CODE)){
									audioUri = createDatabaseAdapter
											.opt1_lang_getAudio_lev1(
													data.getID() - 1, mGender);
								}else if(mSelectedLanguage
										.equals(AppConstant.OPTIONAL2_LANGUAGE_CODE)){
									audioUri = createDatabaseAdapter
											.opt2_lang_getAudio_lev1(
													data.getID() - 1, mGender);
								}else if(mSelectedLanguage
										.equals(AppConstant.OPTIONAL3_LANGUAGE_CODE)){
									audioUri = createDatabaseAdapter
											.opt3_lang_getAudio_lev1(
													data.getID() - 1, mGender);
								}
								else {
									audioUri = createDatabaseAdapter
											.getAudio_lev1(data.getID() - 1,
													mGender);
								}

								try {

									AssetFileDescriptor descriptor = getApplicationContext()
											.getAssets().openFd(audioUri);
									long start = descriptor.getStartOffset();
									long end = descriptor.getLength();
									try {

										MediaPlayer mediaPlayer = new MediaPlayer();
										mediaPlayer.setDataSource(
												descriptor.getFileDescriptor(),
												start, end);
										exceptionOccured = false;
									} catch (Exception e) {
									}
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
										mVoiced = true;
										if (mVoiced == true
												&& mAnimated == true) {
											relLayout.setVisibility(View.GONE);
											Log.i(TAG, "Starting new Activity");

											Intent launchIntent = new Intent(
													mContext,
													CategoryItemsActivity.class);
											launchIntent.putExtra(
													AppConstant.CATEGORY_ID,
													category_id);
											launchIntent.putExtra(
													AppConstant.LEVEL, mLevel);
											launchIntent.putExtra("user",username);
											launchIntent
													.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											mContext.startActivity(launchIntent);
											if (mediaPlayer != null) {
												if (mediaPlayer.isPlaying()) {
													mediaPlayer.stop();
												}
												mediaPlayer.release();
											}
											mediaPlayer = null;
											mAudioMgr
													.abandonAudioFocus(focusListener);
										}
									}
								});
					}
				}
			}
		};

		if (mSelectionMode == AppConstant.SEL_CLICK) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file, true), 8192);
				String Click = "Click mode"; // printing mode of navigation
				writer.write(Click + " " + time + "\n");
				writer.close();
				//Toast.makeText(getBaseContext(), "yes path!!! =)",
				//  Toast.LENGTH_LONG).show();
			}catch (Exception e) {
				//  Toast.makeText(getBaseContext(), "IN EXCEPTION" + e.toString(), Toast.LENGTH_SHORT).show();

			}
			gridView.setOnItemClickListener(mOnItemClick);
		} else if (mSelectionMode == AppConstant.SEL_CROSS_LINE) {
			gridView.setOnItemClickListener(null);
			animateHorizatalLine();
			// initScaningLine();

		} else {
			initPictureScanData();
			gridView.setOnItemClickListener(null);
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					if (mSelectionMode == AppConstant.SEL_HIGHLIGHT
							&& mPictureScanHilight == true) {
						View thumb = gridView.getChildAt(mPictureScanIndex
								- mMinPos);

						if (mPictureScanHilight == true) {
							mOnItemClick.onItemClick(null, thumb,
									mPictureScanIndex, 0);
							if (mSelectionMode == AppConstant.SEL_HIGHLIGHT) {

								if (mTimeHandler != null) {
									mTimeHandler.removeCallbacks(mTimeRunnable);
									mTimeHandler
											.removeCallbacks(mScanningRunnable);
									mTimeHandler
											.removeCallbacks(mEraseRunnable);
									mTimeHandler = null;
								}
							}
						}

					}

				}
			});
		}
		LinearLayout container = (LinearLayout) findViewById(R.id.parent_view);
		container.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (mSelectionMode) {
					case AppConstant.SEL_HIGHLIGHT: {
						if (mPictureScanHilight) {
							View thumb = gridView.getChildAt(mPictureScanIndex
									- mMinPos);
							mOnItemClick.onItemClick(null, thumb,
									mPictureScanIndex, 0);
							//	Toast.makeText(getBaseContext(), "this is my Toast message!!! =)",
							//			Toast.LENGTH_LONG).show(); // added by kasthuri alret message in scan mode working
							if (mTimeHandler != null) {
								mTimeHandler.removeCallbacks(mTimeRunnable);
								mTimeHandler.removeCallbacks(mScanningRunnable);
								mTimeHandler.removeCallbacks(mEraseRunnable);
								mTimeHandler = null;
							}
						}

					}
					break;
					case AppConstant.SEL_CROSS_LINE:
						if (onclikhorizatalLine == false
								&& (onclikVerticleLine == false)) {
							onclikhorizatalLine = true;
							mSelected_y = Horizatal_line.getY();
						} else if ((onclikhorizatalLine == true)
								&& (onclikVerticleLine == false)) {

							onclikVerticleLine = true;
							selected_x = Verticlel_line.getX();
							selectedItem(selected_x, mSelected_y);
							onclikhorizatalLine = false;
							onclikVerticleLine = false;
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
					Log.d("logical", " gridview listener");
					if (mPictureScanHilight == true) {
						View thumb = gridView.getChildAt(mPictureScanIndex
								- mMinPos);
						mOnItemClick.onItemClick(null, thumb,
								mPictureScanIndex, 0);
						if (mTimeHandler != null) {
							mTimeHandler.removeCallbacks(mTimeRunnable);
							mTimeHandler.removeCallbacks(mScanningRunnable);
							mTimeHandler.removeCallbacks(mEraseRunnable);
							mTimeHandler = null;
						}
					}

					break;
				}
				default:
				}
			}
		});

		// [START] fix for the defect: 123
		gridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				Log.d("logical",
						" in the onTouch== onScrollStateChanged scrollState= "
								+ scrollState);
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

		//
		gridView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (mSelectionMode) {

					case AppConstant.SEL_CLICK:

						gridView.setOnItemClickListener(mOnItemClick);


					case AppConstant.SEL_HIGHLIGHT: {

						View thumb = gridView.getChildAt(mPictureScanIndex
								- mMinPos);
					/*if (((mLevel.equalsIgnoreCase("2")) && (mPictureScanIndex == 10))
							|| (((mLevel.equalsIgnoreCase("1")) && (mPictureScanIndex == 4)))) {

						mOnItemClick.onItemClick(null, thumb,
								mPictureScanIndex, 0);*/
						if (mPictureScanHilight == true)
							if (((mLevel.equalsIgnoreCase("2")) && (mPictureScanIndex == 10))
									|| (((mLevel.equalsIgnoreCase("1"))))) {

								mOnItemClick.onItemClick(null, thumb,
										mPictureScanIndex, 0);


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
								mSelected_y = Horizatal_line.getY();
								animatevericalLine();

							} else if ((onclikhorizatalLine == true)
									&& (onclikVerticleLine == false)) {

								onclikVerticleLine = true;
								selected_x = Verticlel_line.getX();
								selectedItem(selected_x, mSelected_y);
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

		if ((mMode.equalsIgnoreCase(getString(R.string.defMode)))
				&& (mSelectionMode == AppConstant.SEL_CLICK)) {
			Log.d(TAG, ">>>Inside register for Context Menu");
			registerForContextMenu(gridView);

		} else {
			unregisterForContextMenu(gridView);
		}
	}

	protected void clearPrefence() {

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
		/* [START]::Added code to Position issue for Label: Lavanya H M */
		if (mSelectionMode == AppConstant.SEL_CLICK) {
			Editor editor = sharedpreferences.edit();
			editor.putInt(username, 0);
			editor.commit();
		}
		/* [END]::Added code to Position issue for Label: Lavanya H M */

	}

	@SuppressLint("NewApi")
	public void zoomImageFromThumb(View thumbView, String uri,
			CharSequence text_label, final int category_id) {
		final FrameLayout frmLayout = (FrameLayout) findViewById(R.id.container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		relLayout = (LinearLayout) inflater.inflate(R.layout.anim_object, null);
		ImageView expandedImage = (ImageView) relLayout
				.findViewById(R.id.anim_image);
		TextView mLabelText = (TextView) relLayout.findViewById(R.id.anim_text);
		mLabelText.setText(text_label);
		// [START] code for Label color change feature
		String labelTextColor = sharedpreferences.getString(AppConstant.PREF_LABEL_COLOR,
				"#0f0f0f");//added by kasthuri changed the default label color to black
		mLabelText.setTextColor(Color.parseColor(labelTextColor));
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
		// [END] code for Label color change feature
		final FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				Gravity.CENTER);

		//frmLayout.setAlpha(0.5f);

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
					mContext, uri, 200, 00);
			expandedImage.setImageBitmap(mAnimBitmap);
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
			float mDeltaWidth = (startWidth - startBounds.width()) / 2;
			startBounds.left -= mDeltaWidth;
			startBounds.right += mDeltaWidth;
		} else {
			// Extend start bounds vertically
			startScale = (float) startBounds.width() / finalBounds.width();
			endScaleX = (float) finalBounds.width() / intrinsicWidth;
			endScaleY = (float) finalBounds.height() / intrinsicHeight;
			float startHeight = startScale * finalBounds.height();
			float mDeltaHeight = (startHeight - startBounds.height()) / 2;
			startBounds.top -= mDeltaHeight;
			startBounds.bottom += mDeltaHeight;
		}

		int mShortAnimationDuration = getResources().getInteger(
				android.R.integer.config_shortAnimTime);
		mShortAnimationDuration *= mAnimSpeed;
		AnimatorSet set = new AnimatorSet();
		/*Values modified by kasthuri*/

			set.play(
					ObjectAnimator.ofFloat(relLayout, View.SCALE_X, startScale,
							endScaleX * 0.50f)).with(
					ObjectAnimator.ofFloat(relLayout, View.SCALE_Y, startScale,
							endScaleY * 0.50f));


		gridView.setAlpha(0.0f); //added by kasthuri to blur background
		Log.d(TAG, "Scales are " + startScale + "  end scale x" + endScaleX
				+ "  end scale y" + endScaleY);
		Log.d(TAG, "Scales are intrinsic height and width are "
				+ intrinsicHeight + "  " + intrinsicWidth);
		set.setDuration(mShortAnimationDuration);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				//		Toast.makeText(getBaseContext(), "this is my Toast message!!! =)",
				//			Toast.LENGTH_LONG).show();
				//			Toast.makeText(getApplicationContext(),username,Toast.LENGTH_SHORT).show();
				mAnimated = true;
				SystemClock.sleep(3000);//added by kasthuri to keep the animated image on screen for few seconds
				relLayout.setVisibility(View.GONE);
				gridView.setAlpha(1.0f);//added by kasthuri to undo the blur
				invalidateOptionsMenu();
				//		Toast.makeText(getApplicationContext(),username,Toast.LENGTH_SHORT).show();
				if (mVoiced == true && mAnimated == true) {
					mCurrentAnimator = null;
					//File file = new File(Environment.getExternalStorageDirectory() + File.separator + "test.txt");
					Intent launchIntent = new Intent(mContext,
							CategoryItemsActivity.class);
					launchIntent.putExtra(AppConstant.CATEGORY_ID, category_id);
					launchIntent.putExtra(AppConstant.LEVEL, mLevel);
					launchIntent.putExtra("hexcolor", hexColor);
					launchIntent.putExtra("user", username);
					launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(launchIntent);
					//	Toast.makeText(getBaseContext(), "this is my Toast message!!! =)",
					//				Toast.LENGTH_LONG).show(); //added by kasthuri alert message comes on click mode working


				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {

				mCurrentAnimator = null;
			}
		});
		set.start();
		mCurrentAnimator = set;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		//clearPrefence();
		// [START] of fix to defect 74
		if (mSelectionMode == AppConstant.SEL_CROSS_LINE) {
			initScaningLine();
		}
		// [END] of fix to defect 74

	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
		wl.release();
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
				handlerHor.removeCallbacks(mHorizatalLineRunnable);
				handlerHor = null;
			}

			if (handlerVer != null) {
				handlerVer.removeCallbacks(mVerticleLineRunnable);
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
		if (mAudioMgr != null) {
			mAudioMgr.abandonAudioFocus(focusListener);
		}

		scrollIndex = gridView.getFirstVisiblePosition();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (mAnimated == true) {
			MenuInflater menuInflater = getMenuInflater();
			menuInflater.inflate(R.menu.main_menu, menu);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		if (mSelectionMode == AppConstant.SEL_CROSS_LINE) {
			menu.findItem(R.id.scan_line).setVisible(true);
			menu.findItem(R.id.scan_line).setEnabled(true);

			menu.findItem(R.id.scan_line_thickness).setVisible(true);
			menu.findItem(R.id.scan_line_thickness).setEnabled(true);
		} else {
			menu.findItem(R.id.scan_line).setVisible(false);
			menu.findItem(R.id.scan_line).setEnabled(false);

			menu.findItem(R.id.scan_line_thickness).setVisible(false);
			menu.findItem(R.id.scan_line_thickness).setEnabled(false);
		}

		if(mSelectionMode == AppConstant.SEL_HIGHLIGHT){
			menu.findItem(R.id.border).setVisible(true);
		}else{
			menu.findItem(R.id.border).setVisible(false);
		}
		if ((mSelectionMode == AppConstant.SEL_CLICK)
				&& (mMode.equals(getResources().getString(R.string.defMode)))) {
			menu.findItem(R.id.language_selected).setVisible(true);
			menu.findItem(R.id.language_selected).setEnabled(true);
		} else {
			menu.findItem(R.id.language_selected).setVisible(false);
			menu.findItem(R.id.language_selected).setEnabled(false);
		}

		if (mAnimated == false) {

			return false;
		} else {

			return super.onPrepareOptionsMenu(menu);
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if (mAnimated == false) {
			return false;
		}
		switch (item.getItemId()) {

		case R.id.settings:

			Intent settingsIntent = new Intent(getBaseContext(),
					Prefsclass.class);
			settingsIntent.putExtra("user", username);
			settingsIntent.putExtra("screensize", screenSize);
			startActivity(settingsIntent);
			setmReturn_PrefsActivtyboolVal(true);
			break;

		case R.id.share:
			shareTextUrl(); // function to share to email
			/*final Dialog dialog1 = new Dialog(mContext);

			dialog1.requestWindowFeature(Window.FEATURE_LEFT_ICON);
			dialog1.setContentView(R.layout.custom_dilaog);
			dialog1.setTitle(R.string.share);

			dialog1.show();*/
			break;

		case R.id.details:
			// AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			// alertDialog.setTitle(R.string.app_name);
			// alertDialog.setMessage(getString(R.string.version));
			// alertDialog.setButton("OK", new DialogInterface.OnClickListener()
			// {
			// public void onClick(DialogInterface dialog, int which) {
			// }
			// });
			// alertDialog.show();
			final Dialog dialog = new Dialog(mContext);

			dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
			dialog.setContentView(R.layout.custom_dilaog);
			dialog.setTitle(R.string.details);

			dialog.show();
			// [START] to fix defect 121
			TextView versionText = (TextView) dialog
					.findViewById(R.id.id_versionTextView);
			versionText.setText("Version: " + versionText.getText().toString());
			// [END] to fix defect 121

			TextView url_launcher = (TextView) dialog
					.findViewById(R.id.text_url_link);
			url_launcher.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					try {
						Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri
								.parse(getString(R.string.download_link)));
						startActivity(myIntent);
					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
				}
			});

			Button btn_okey = (Button) dialog.findViewById(R.id.button_ok);
			btn_okey.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.cancel();
				}
			});
			break;

		case R.id.back_ground:
			getselectedcolor(R.id.back_ground);
			break;

		case R.id.border:
				getselectedcolor(R.id.border);
				break;

		case R.id.scan_line:
			getselectedcolor(R.id.scan_line);
			break;

		case R.id.scan_line_thickness:
			setlinethickness();
			break;
		case R.id.language_selected:
			getselectedLanguage();
			break;
		case R.id.KaviPro:
			boolean kavipro_installed = appInstalledOrNot("com.enability.kavipro");
			// [END] fix for bug 129
			if (!kavipro_installed) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						mContext);

				// set title
				alertDialogBuilder
						.setTitle("Do you want install Kavi Pro application ");

				// set dialog message
				alertDialogBuilder
						.setMessage("Click yes to confirm....")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog,
											int id) {

										try {
											Intent myIntent = new Intent(
													Intent.ACTION_VIEW,
													Uri.parse(getString(R.string.kavipro_download_link)));
											startActivity(myIntent);
										} catch (ActivityNotFoundException e) {
											e.printStackTrace();
										}
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}

/*				AssetManager assetManager = getAssets();

				InputStream in = null;
				OutputStream out = null;

				try {
					in = assetManager.open("Kavi-Pro-debug.apk");
					out = new FileOutputStream("/sdcard/Kavi-Pro-debug.apk");

					byte[] buffer = new byte[1024];

					int read;
					while((read = in.read(buffer)) != -1) {

						out.write(buffer, 0, read);

					}

					in.close();
					in = null;

					out.flush();
					out.close();
					out = null;

					Intent intent = new Intent(Intent.ACTION_VIEW);

					intent.setDataAndType(Uri.fromFile(new File("/sdcard/Kavi-Pro-debug.apk")),
							"application/vnd.android.package-archive");

					startActivity(intent);

				} catch(Exception e) { }
*/

				break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void shareTextUrl() {
		Intent newintent = getIntent();
		username = newintent.getExtras().getString("user");
		User = username;
		//Toast.makeText(getApplicationContext(),username,Toast.LENGTH_SHORT).show();

		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND_MULTIPLE);
		String aEmailList[] = { "enabilityfoundation@gmail.com" };
		intent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
		intent.putExtra(Intent.EXTRA_SUBJECT, "Data collected for kavi, username:" + " " + username);
		intent.setType("image/jpeg"); /* This example is sharing jpeg images. */
		ArrayList<Uri> files = new ArrayList<Uri>();
		String externalStoragePathStr = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).toString() + File.separatorChar;
		String filePaths[] = {
				externalStoragePathStr + filename_date};
				//externalStoragePathStr + filename2};
		for (String path : filePaths /* List of the files you want to send */) {
			File file = new File(path);
			Uri uri = Uri.fromFile(file);
			files.add(uri);
		}

		intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
		startActivity(intent);
		finish();
	}

	private void getselectedLanguage() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridBackGround1 = inflater.inflate(
				R.layout.language_selection_menu, null);

		Button btn_confirm = (Button) gridBackGround1
				.findViewById(R.id.btn_t_confirm);
		Button btnCancel = (Button) gridBackGround1
				.findViewById(R.id.btn_t_cancel);

		AlertDialog.Builder grdBuilder1 = new AlertDialog.Builder(mContext);
		grdBuilder1.setView(gridBackGround1);
		grdBuilder1.setCancelable(false);

		final AlertDialog grdDialog1 = grdBuilder1.create();
		grdDialog1.show();
		mSelectedLanguage = sharedpreferences.getString(AppConstant.PREF_SELECTED_LANGUAGE,
				"en");
		if (true == (mSelectedLanguage
				.equals(AppConstant.OPTIONAL_LANGUAGE_CODE))) {
			// [START] fix for defect 113
			mSelectedLanguage = AppConstant.OPTIONAL_LANGUAGE_CODE;
			// [END] fix for defect 113
			RadioButton radioButton = (RadioButton) grdDialog1
					.findViewById(R.id.optional_language);
			radioButton.setChecked(true);

		}else if(true == (mSelectedLanguage
				.equals(AppConstant.OPTIONAL1_LANGUAGE_CODE))){
			mSelectedLanguage = AppConstant.OPTIONAL1_LANGUAGE_CODE;
			// [END] fix for defect 113
			RadioButton radioButton = (RadioButton) grdDialog1
					.findViewById(R.id.optional_language1);
			radioButton.setChecked(true);

		}else if(true == (mSelectedLanguage
				.equals(AppConstant.OPTIONAL2_LANGUAGE_CODE))){
			mSelectedLanguage = AppConstant.OPTIONAL2_LANGUAGE_CODE;
			// [END] fix for defect 113
			RadioButton radioButton = (RadioButton) grdDialog1
					.findViewById(R.id.optional_language2);
			radioButton.setChecked(true);

		}else if(true == (mSelectedLanguage
				.equals(AppConstant.OPTIONAL3_LANGUAGE_CODE))){
			mSelectedLanguage = AppConstant.OPTIONAL3_LANGUAGE_CODE;
			// [END] fix for defect 113
			RadioButton radioButton = (RadioButton) grdDialog1
					.findViewById(R.id.optional_language3);
			radioButton.setChecked(true);

		}
		else {
			// [START] fix for defect 113
			mSelectedLanguage = AppConstant.DEFAULT_LANGUAGE_CODE;
			// [END] fix for defect 113
			RadioButton radioButton = (RadioButton) grdDialog1
					.findViewById(R.id.default_language);
			radioButton.setChecked(true);
		}

		btn_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				SharedPreferences.Editor editor = sharedpreferences.edit();
				editor.putString(AppConstant.PREF_SELECTED_LANGUAGE,
						String.valueOf(mSelectedLanguage));
				editor.commit();

				if (true == (mSelectedLanguage
						.equals(AppConstant.OPTIONAL_LANGUAGE_CODE))) {
					mLocale = new Locale(mSelectedLanguage);
					Locale.setDefault(mLocale);
					Configuration config = new Configuration();
					config.locale = mLocale;

					getBaseContext().getResources()
							.updateConfiguration(
									config,
									getBaseContext().getResources()
											.getDisplayMetrics());
					highLevelDataList = FrontController.getInstance(mContext)
							.fetchHighLevelData(mSelectedLanguage, mLevel);
					imageAdapter.notifyDataSetChanged();
					// [START] fix for bug 129
					boolean installed = appInstalledOrNot("com.sriandroid.justkannada");
					// [END] fix for bug 129
					if (!installed) {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								mContext);

						// set title
						alertDialogBuilder
								.setTitle("Do you want install Language support application ");

						// set dialog message
						alertDialogBuilder
								.setMessage("Click yes to confirm....")
								.setCancelable(false)
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {

												try {
													Intent myIntent = new Intent(
															Intent.ACTION_VIEW,
															Uri.parse(getString(R.string.language_download_link)));
													startActivity(myIntent);
												} catch (ActivityNotFoundException e) {
													e.printStackTrace();
												}
											}
										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.cancel();
											}
										});

						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}

				}
				if (true == (mSelectedLanguage
						.equals(AppConstant.OPTIONAL1_LANGUAGE_CODE))) {

					mLocale = new Locale(mSelectedLanguage);
					Locale.setDefault(mLocale);
					Configuration config = new Configuration();
					config.locale = mLocale;

					getBaseContext().getResources()
							.updateConfiguration(
									config,
									getBaseContext().getResources()
											.getDisplayMetrics());
					highLevelDataList = FrontController.getInstance(mContext)
							.fetchHighLevelData(mSelectedLanguage, mLevel);
					imageAdapter.notifyDataSetChanged();

					boolean tamil_installed = appInstalledOrNot("com.anysoftkeyboard.languagepack.tamil");
					// [END] fix for bug 129
					if (!tamil_installed) {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								mContext);

						// set title
						alertDialogBuilder
								.setTitle("Do you want install Language support application ");

						// set dialog message
						alertDialogBuilder
								.setMessage("Click yes to confirm....")
								.setCancelable(false)
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {

												try {
													Intent myIntent = new Intent(
															Intent.ACTION_VIEW,
															Uri.parse(getString(R.string.tamil_language_download_link)));
													startActivity(myIntent);
													Intent myIntentnew = new Intent(
															Intent.ACTION_VIEW,
															Uri.parse(getString(R.string.english_language_download_link)));
													startActivity(myIntentnew);
												} catch (ActivityNotFoundException e) {
													e.printStackTrace();
												}
											}
										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.cancel();
											}
										});

						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}


				}
				if (true == (mSelectedLanguage
						.equals(AppConstant.OPTIONAL2_LANGUAGE_CODE))) {

					mLocale = new Locale(mSelectedLanguage);
					Locale.setDefault(mLocale);
					Configuration config = new Configuration();
					config.locale = mLocale;

					getBaseContext().getResources()
							.updateConfiguration(
									config,
									getBaseContext().getResources()
											.getDisplayMetrics());
					highLevelDataList = FrontController.getInstance(mContext)
							.fetchHighLevelData(mSelectedLanguage, mLevel);
					imageAdapter.notifyDataSetChanged();

				}

				if (true == (mSelectedLanguage
						.equals(AppConstant.OPTIONAL3_LANGUAGE_CODE))) {

					mLocale = new Locale(mSelectedLanguage);
					Locale.setDefault(mLocale);
					Configuration config = new Configuration();
					config.locale = mLocale;

					getBaseContext().getResources()
							.updateConfiguration(
									config,
									getBaseContext().getResources()
											.getDisplayMetrics());
					highLevelDataList = FrontController.getInstance(mContext)
							.fetchHighLevelData(mSelectedLanguage, mLevel);
					imageAdapter.notifyDataSetChanged();

				}

				if (true == (mSelectedLanguage
						.equals(AppConstant.DEFAULT_LANGUAGE_CODE))) {

					mLocale = new Locale(mSelectedLanguage);
					Locale.setDefault(mLocale);
					Configuration config = new Configuration();
					config.locale = mLocale;

					getBaseContext().getResources()
							.updateConfiguration(
									config,
									getBaseContext().getResources()
											.getDisplayMetrics());
					highLevelDataList = FrontController.getInstance(mContext)
							.fetchHighLevelData(mSelectedLanguage, mLevel);
					imageAdapter.notifyDataSetChanged();

				}
				grdDialog1.dismiss();

			}
		});

		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				mSelectedLanguage = sharedpreferences.getString(
						AppConstant.PREF_SELECTED_LANGUAGE, "en");
				mLocale = new Locale(mSelectedLanguage);
				Locale.setDefault(mLocale);
				Configuration config = new Configuration();
				config.locale = mLocale;
				grdDialog1.dismiss();
			}
		});

	}

	public void onlanguageRadioButtonClicked(View view) {

		boolean checked = ((RadioButton) view).isChecked();
		switch (view.getId()) {
		case R.id.default_language:

			if (checked) {
				mSelectedLanguage = AppConstant.DEFAULT_LANGUAGE_CODE;
			}
			break;
		case R.id.optional_language:
			if (checked) {
				mSelectedLanguage = AppConstant.OPTIONAL_LANGUAGE_CODE;
			}
			break;
		case R.id.optional_language1:
			if(checked){
				mSelectedLanguage = AppConstant.OPTIONAL1_LANGUAGE_CODE;
			}
			break;
		case R.id.optional_language2:
			if(checked){
				mSelectedLanguage = AppConstant.OPTIONAL2_LANGUAGE_CODE;
			}
			break;
		case R.id.optional_language3:
			if(checked){
				mSelectedLanguage = AppConstant.OPTIONAL3_LANGUAGE_CODE;
			}
			break;
		}


	}

	private void setlinethickness() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridBackGround1 = inflater.inflate(R.layout.line_thickness_menu,
				null);

		Button btn_confirm1 = (Button) gridBackGround1
				.findViewById(R.id.btn_t_confirm);
		Button btnCancel = (Button) gridBackGround1
				.findViewById(R.id.btn_t_cancel);

		AlertDialog.Builder grdBuilder1 = new AlertDialog.Builder(mContext);
		grdBuilder1.setView(gridBackGround1);
		grdBuilder1.setCancelable(false);

		final AlertDialog grdDialog1 = grdBuilder1.create();
		grdDialog1.show();
		mScanLinethickness = Integer.parseInt(sharedpreferences.getString(
				AppConstant.PREF_SCANLINE_THICKNESS, "5"));
		switch (mScanLinethickness) {

		case AppConstant.THICKLINE: {
			RadioButton radioButton = (RadioButton) grdDialog1
					.findViewById(R.id.thickline);
			radioButton.setChecked(true);
		}
			break;

		case AppConstant.MEDIUMLINE: {
			RadioButton radioButton = (RadioButton) grdDialog1
					.findViewById(R.id.mediumline);
			radioButton.setChecked(true);

		}

			break;
		case AppConstant.THINLINE: {
			RadioButton radioButton = (RadioButton) grdDialog1
					.findViewById(R.id.thinline);
			radioButton.setChecked(true);
		}

			break;
		default:
			break;
		}
		btn_confirm1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences.Editor editor = sharedpreferences.edit();
				editor.putString(AppConstant.PREF_SCANLINE_THICKNESS,
						String.valueOf(mScanLinethickness));
				//String filename = username + ".log";
				File file = new File(path, filename_date);
				BufferedReader br = null;
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(file, true), 8192);
					writer.write("line thickness" + " " + mScanLinethickness + " " + time + "\n");
					writer.close();
					//Toast.makeText(getBaseContext(), "yes path!!! =)",
					//  Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					//  Toast.makeText(getBaseContext(), "IN EXCEPTION" + e.toString(), Toast.LENGTH_SHORT).show();

				}
				editor.commit();
				grdDialog1.dismiss();
			}
		});

		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mScanLinethickness = Integer.parseInt(sharedpreferences.getString(
						AppConstant.PREF_SCANLINE_THICKNESS, "5"));
				layoutParamsHor = Horizatal_line.getLayoutParams();
				layoutParamsHor.height = mScanLinethickness;
				Horizatal_line.setLayoutParams(layoutParamsHor);
				layoutParamsVer = Verticlel_line.getLayoutParams();
				layoutParamsVer.width = mScanLinethickness;
				Verticlel_line.setLayoutParams(layoutParamsVer);
				grdDialog1.dismiss();

			}
		});

	}

	public void onRadioButtonClicked(View view) {

		boolean checked = ((RadioButton) view).isChecked();
		switch (view.getId()) {
		case R.id.thickline:

			if (checked) {
				layoutParamsHor = Horizatal_line.getLayoutParams();
				layoutParamsHor.height = AppConstant.THICKLINE;
				Horizatal_line.setLayoutParams(layoutParamsHor);
				layoutParamsVer = Verticlel_line.getLayoutParams();
				layoutParamsVer.width = AppConstant.THICKLINE;
				Verticlel_line.setLayoutParams(layoutParamsVer);
				mScanLinethickness = AppConstant.THICKLINE;

			}
			break;
		case R.id.thinline:
			if (checked) {

				layoutParamsHor = Horizatal_line.getLayoutParams();
				layoutParamsHor.height = AppConstant.THINLINE;
				Horizatal_line.setLayoutParams(layoutParamsHor);
				layoutParamsVer = Verticlel_line.getLayoutParams();
				layoutParamsVer.width = AppConstant.THINLINE;
				Verticlel_line.setLayoutParams(layoutParamsVer);
				mScanLinethickness = AppConstant.THINLINE;
			}
			break;
		case R.id.mediumline:
			if (checked) {

				layoutParamsHor = Horizatal_line.getLayoutParams();
				layoutParamsHor.height = AppConstant.MEDIUMLINE;
				Horizatal_line.setLayoutParams(layoutParamsHor);
				layoutParamsVer = Verticlel_line.getLayoutParams();
				layoutParamsVer.width = AppConstant.MEDIUMLINE;
				Verticlel_line.setLayoutParams(layoutParamsVer);
				mScanLinethickness = AppConstant.MEDIUMLINE;
			}
			break;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);
		AdapterContextMenuInfo cmi = (AdapterContextMenuInfo) menuInfo;
		Log.i(TAG, ">>>>>>>" + cmi.position);
		mSelected_ID = highLevelDataList.get(cmi.position).getID();
		if (cmi.position < gridMaxNum) {
			i = mSelected_ID;
			menu.setHeaderTitle("Options");

			Log.i(TAG, ">>>>>>>mSelected_ID:" + mSelected_ID);
			/* [START]::Added code to Position issue for Label: Lavanya H M */
			if (mSelectionMode == AppConstant.SEL_CLICK) {
				Editor editor = sharedpreferences.edit();
				editor.putInt(username, cmi.position);
				editor.commit();
			}
			/* [END]::Added code to Position issue for Label: Lavanya H M */
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
		//	Intent photoPickerIntent = new Intent(new Intent(Intent.ACTION_PICK,
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
							mLabel = input.getText().toString();
							if (!(mLabel.trim().equalsIgnoreCase(""))) {
								//Toast.makeText(getApplicationContext(), "Your selected language is " + mSelectedLanguage, Toast.LENGTH_SHORT).show();
								//Toast.makeText(getApplicationContext(), "Your mSelected_ID is " + mSelected_ID, Toast.LENGTH_SHORT).show();
								//Toast.makeText(getApplicationContext(), "Your mLabel is " + mLabel, Toast.LENGTH_SHORT).show();
								//Toast.makeText(getApplicationContext(), "Your mLevel is " + mLevel, Toast.LENGTH_SHORT).show();
								frontController.updateLabelData(
										mSelectedLanguage, mLevel,
										mSelected_ID, mLabel);
								highLevelDataList = frontController
										.fetchHighLevelData(mSelectedLanguage,
												mLevel);
								imageAdapter.notifyDataSetChanged();
								//
							} else {
								showAlertOk(HomeActivity.this, "Error",
										"The Label cannot be empty");
							}
						}
					});
			editalert.show();

			break;

		case AppConstant.GROUP_ID_RESET:
			Log.i(TAG, "Selected Language is = " + mSelectedLanguage);
			//Toast.makeText(getApplicationContext(), "Your selected language is " + mSelectedLanguage, Toast.LENGTH_SHORT).show();
			//Toast.makeText(getApplicationContext(), "Your mSelected_ID is " + mSelected_ID, Toast.LENGTH_SHORT).show();
			//Toast.makeText(getApplicationContext(), "Your mLabel is " + mLabel, Toast.LENGTH_SHORT).show();
			//Toast.makeText(getApplicationContext(), "Your mLevel is " + mLevel, Toast.LENGTH_SHORT).show();
			frontController.resetItem(mLevel, mSelectedLanguage, mSelected_ID);
			// [START] to fix for defect 114
			highLevelDataList = frontController.fetchHighLevelData(
					mSelectedLanguage, mLevel);
			// [END] to fix for defect 114
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

		//
		// case AppConstant.SETTINGS_CHANGED:
		// finish();
		// Intent launch = new Intent(getApplicationContext(),
		// HomeActivity.class);
		// startActivity(launch);
		// break;
		//

			case AppConstant.PICKPHOTO_RESULT_CODE:

				if (resultCode == RESULT_OK) {
					Uri selectedImage = resultData.getData();
					String selimgpath = selectedImage.getPath();
					//selimgpath = selimgpath.replace(":","/");
					Bitmap bitmap = null;
					Log.i(TAG, "the imagepath is " + selectedImage);
					Log.i(TAG, "The image path is " + selimgpath);


    /* now extract ID from Uri path using getLastPathSegment() and then split with ":"
    then call get Uri to for Internal storage or External storage for media I have used getUri()
    */


// By using this method get the Uri of Internal/External Storage for Media

					// gridView.setSelection(mSelected_ID-1);

					if (selectedImage.getScheme().toString().compareTo("content") == 0) {
						Log.i(TAG, "inside if " + selimgpath);
						selimgpath = getPaths(mContext, selectedImage);
						Log.i(TAG, "getRealPathFromURI " + selimgpath);

						//	key=getKey();
						File file = new File(selimgpath);
						if (file.exists() != true) {
							Log.i(TAG, "inside file exists " + selimgpath);
							selimgpath = null;

						} else {
							if(selimgpath.contains("KAVIPRO_")) {
								Log.i(TAG, "inside file KAVIPRO_ " + selimgpath);
								replace_picture = true;
								try {
								//	Toast.makeText(getApplicationContext(), selimgpath, Toast.LENGTH_SHORT).show();
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
								//Toast.makeText(HomeActivity.this, "after ecrypt", Toast.LENGTH_LONG).show();
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
								//		selimgpath = getFilesDir() + "/" + temp;
								if (file.exists() != true) {
									selimgpath = null;

								}


						} else {
							selimgpath = selectedImage.getPath();
							//	selimgpath = getFilesDir() + "/" + temp;
						}

						if (selimgpath != null) {
							if(replace_picture == true) {
								replace_picture = false;
								selimgpath = getFilesDir() + "/" + temp;
							}else{

							}
							frontController.updatePictureData(mSelectedLanguage,
									mLevel, mSelected_ID, selimgpath);
							highLevelDataList = frontController.fetchHighLevelData(
									mSelectedLanguage, mLevel);
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
					frontController.updateAudioData(mSelectedLanguage, mLevel,
							mSelected_ID, audioFileStr, mGender);// gender argument added by kasthuri to solve replace audio option under female voice
					highLevelDataList = frontController.fetchHighLevelData(
							mSelectedLanguage, mLevel);
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



	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(contentUri, null, null,
				null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
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

	public String getImagePath(Uri uri){
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		String document_id = cursor.getString(0);
		document_id = document_id.substring(document_id.lastIndexOf(":")+1);
		cursor.close();

		cursor = getContentResolver().query(
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
		String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
		cursor.moveToFirst();
		cursor.close();

		return path;
	}


	public String getRealPathPICFromURI(Uri contentUri) {
		String id = contentUri.getLastPathSegment().split(":")[1];
		final String[] imageColumns = {MediaStore.Images.Media.DATA};
		final String imageOrderBy = null;

		Uri uri = getUri();
		String selectedImagePath = "path";

		Cursor imageCursor = managedQuery(uri, imageColumns,
				MediaStore.Images.Media._ID + "=" + id, null, imageOrderBy);

		if (imageCursor.moveToFirst()) {
			selectedImagePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
		}
		Log.e("path", selectedImagePath); // use selectedImagePath
		return selectedImagePath;
	}

	private Uri getUri() {
		String state = Environment.getExternalStorageState();
		if(!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
			return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

		return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	}

	void animateHorizatalLine() {

		handlerHor = new Handler();
		mHorizatalLineRunnable = new Runnable() {

			public void run() {
				try {
					mhorizatal_y = Horizatal_line.getY();
					mhorizatal_y = mhorizatal_y + mDelta;
					Horizatal_line.setY(mhorizatal_y);

					if ((mhorizatal_y <= gridView.getBottom())
							&& (onclikhorizatalLine == false)) {
						handlerHor.removeCallbacks(mHorizatalLineRunnable);

						handlerHor.postDelayed(mHorizatalLineRunnable,
								mScanSpeed);

					} else if ((mhorizatal_y > gridView.getBottom())
							&& (onclikhorizatalLine == false)) {

						if ((gridView.getLastVisiblePosition() < gridMaxNum)
								|| (mGridscanPos < gridMaxNum)) {

							if (mGridscanDelta == 0) {
								mGridscanDelta = mColumnsCount + 1;
								mGridscanPos = gridView
										.getLastVisiblePosition();
							}
							if (mGridscanPos == 0) {
								mGridscanPos = gridView
										.getLastVisiblePosition();
							}
							mGridscanPos = mGridscanPos + mGridscanDelta;
							if (mGridscanPos >= gridView.getCount()) {
								mGridscanPos = gridMaxNum;
							}
							gridView.smoothScrollToPosition(mGridscanPos);
						} else {
							adjustGridView();
						}
						mhorizatal_y = gridView.getTop();
						Horizatal_line.setY(mhorizatal_y);
						handlerHor.removeCallbacks(mHorizatalLineRunnable);
						handlerHor.postDelayed(mHorizatalLineRunnable,
								mScanSpeed);
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
						mSelected_y = Horizatal_line.getY();
						Log.d(TAG,
								"gridView.setOnItemClickListener( --  mSelected_y "
										+ mSelected_y);
						animatevericalLine();
						handlerHor.removeCallbacks(mHorizatalLineRunnable);

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
						mSelected_y = Horizatal_line.getY();
						animatevericalLine();
						handlerHor.removeCallbacks(mHorizatalLineRunnable);

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
						mSelected_y = Horizatal_line.getY();
						animatevericalLine();
					} else if ((onclikhorizatalLine == true)
							&& (onclikVerticleLine == false)) {
						onclikVerticleLine = true;
					}
				}
			}
		});
		handlerHor.removeCallbacks(mHorizatalLineRunnable);
		handlerHor.postDelayed(mHorizatalLineRunnable, mScanSpeed);

	}

	private void animatevericalLine() {
		handlerVer = new Handler();
		mVerticleLineRunnable = new Runnable() {

			public void run() {
				try {

					mVerticle_x = Verticlel_line.getX();
					mVerticle_x = mVerticle_x + mDelta;
					Verticlel_line.setX(mVerticle_x);
					if ((mVerticle_x <= gridView.getWidth())
							&& (onclikhorizatalLine == true)
							&& (onclikVerticleLine == false)) {
						handlerVer.removeCallbacks(mVerticleLineRunnable);

						handlerVer.postDelayed(mVerticleLineRunnable,
								mScanSpeed);

					} else if ((mVerticle_x > gridView.getWidth())
							&& (onclikhorizatalLine == true)
							&& (onclikVerticleLine == false)) {

						mVerticle_x = gridView.getLeft();
						Verticlel_line.setX(mVerticle_x);
						handlerVer.removeCallbacks(mVerticleLineRunnable);
						handlerVer.postDelayed(mVerticleLineRunnable,
								mScanSpeed);

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
						selectedItem(selected_x, mSelected_y);
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
						selectedItem(selected_x, mSelected_y);
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
						selected_x = Verticlel_line.getX();
						selectedItem(selected_x, mSelected_y);
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
						selectedItem(selected_x, mSelected_y);
						onclikhorizatalLine = false;
						onclikVerticleLine = false;

					}
				}
			}
		});
		handlerVer.removeCallbacks(mVerticleLineRunnable);
		handlerVer.postDelayed(mVerticleLineRunnable, mScanSpeed);
	}

	public void selectedItem(float selected_x2, float mSelected_y2) {

		int position = gridView.pointToPosition((int) selected_x,
				(int) mSelected_y);
		int FirstVisiblePosition = gridView.getFirstVisiblePosition();
		View thumb = gridView.getChildAt(position - FirstVisiblePosition);
		if (thumb == null) {
			showAlertOk(mContext, "Wrong Selection",
					"Empty space selection is not allowed");

			onclikhorizatalLine = false;
			onclikVerticleLine = false;
			// [Start] fix to defect 102
			if (handlerHor != null) {
				handlerHor.removeCallbacks(mHorizatalLineRunnable);
				handlerHor = null;
			}
			if (handlerVer != null) {
				handlerVer.removeCallbacks(mVerticleLineRunnable);
				handlerVer = null;
			}
			initScaningLine();
			updateUI();
			// [END] fix to defect 102
		} else {
			mOnItemClick.onItemClick(null, thumb, position, 0);
		}

	}

	public void getselectedcolor(final int itemId) {

		final ArrayList<String> mColorList = AppConstant
				.initializeColorArraylist();
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridBackGround = inflater.inflate(R.layout.colors_grid_menu, null);
		btn_confirm = (Button) gridBackGround.findViewById(R.id.btn_confirm);
		Button btnCancel = (Button) gridBackGround
				.findViewById(R.id.btn_cancel);

		GridView grd = (GridView) gridBackGround.findViewById(R.id.gridview2);
		GridViewAdapter adapter = new GridViewAdapter(mContext, mColorList);
		grd.setAdapter(adapter);
		AlertDialog.Builder grdBuilder = new AlertDialog.Builder(mContext);
		grdBuilder.setView(gridBackGround);
		grdBuilder.setCancelable(false);
		final AlertDialog grdDialog = grdBuilder.create();

		grdDialog.show();
		WindowManager.LayoutParams params = grdDialog.getWindow()
				.getAttributes();
		if(screenSize == 2){
		//	Toast.makeText(getApplicationContext(), "Your screensize is " + screenSize,  Toast.LENGTH_SHORT).show();
			params.width = 50 * 10;
		}else {
		//	Toast.makeText(getApplicationContext(), "Your screensize is " + screenSize,  Toast.LENGTH_SHORT).show();
			params.width = 50 * 12;
		}
		grdDialog.getWindow().setAttributes(params);
		grd.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mColorposition = arg2;

				switch (itemId) {
				case R.id.scan_line: {
					String linecolor = String.format("#%06X", (0xFFFFFF & Color.parseColor(mColorList.get(mColorposition))));
					File file = new File(path, filename_date);
					BufferedReader br = null;
					try {
						BufferedWriter writer = new BufferedWriter(new FileWriter(file, true), 8192);
						writer.write("scanline color is" + " " + linecolor + " " + time + "\n"); //printing background color
						writer.close();
						//Toast.makeText(getBaseContext(), "yes path!!! =)",
						//  Toast.LENGTH_LONG).show();
					}catch (Exception e) {
						//  Toast.makeText(getBaseContext(), "IN EXCEPTION" + e.toString(), Toast.LENGTH_SHORT).show();

					}
					Horizatal_line.setBackgroundColor(Color
							.parseColor(mColorList.get(mColorposition)));
					Verticlel_line.setBackgroundColor(Color
							.parseColor(mColorList.get(mColorposition)));
					btn_confirm.setEnabled(true);
				}
					break;
				case R.id.back_ground: {
					String bgcolor = String.format("#%06X", (0xFFFFFF & Color.parseColor(mColorList.get(mColorposition))));
				//	String filename = username + ".log";
					File file = new File(path, filename_date);
					BufferedReader br = null;
					try {
						BufferedWriter writer = new BufferedWriter(new FileWriter(file, true), 8192);
						writer.write("background color is" + " " + bgcolor + " " + time + "\n"); //printing background color
						writer.close();
						//Toast.makeText(getBaseContext(), "yes path!!! =)",
						//  Toast.LENGTH_LONG).show();
					}catch (Exception e) {
						//  Toast.makeText(getBaseContext(), "IN EXCEPTION" + e.toString(), Toast.LENGTH_SHORT).show();

					}
					LinearLayout ll = (LinearLayout) findViewById(R.id.parent_view);
					ll.setBackgroundColor(Color.parseColor(mColorList
							.get(mColorposition)));
					ColorSingleton.getInstance().setColor(
							mColorList.get(mColorposition));
					redrawbackground();
					btn_confirm.setEnabled(true);
				}
					break;

				case R.id.border: { //added by kasthuri to change the scan border color
					//String hexColor = Integer.toHexString(mColorposition).toUpperCase();
					hexColor = String.format("#%06X", (0xFFFFFF & Color.parseColor(mColorList.get(mColorposition))));
					//String filename = username + ".log";
					File file = new File(path, filename_date);
					BufferedReader br = null;					try {
						BufferedWriter writer = new BufferedWriter(new FileWriter(file, true), 8192);
						writer.write("border color" + " " + hexColor + " " + time + "\n"); //printing scan border color
						writer.close();
						//Toast.makeText(getBaseContext(), "yes path!!! =)",
						//  Toast.LENGTH_LONG).show();
					}catch (Exception e) {
						//  Toast.makeText(getBaseContext(), "IN EXCEPTION" + e.toString(), Toast.LENGTH_SHORT).show();

					}
					btn_confirm.setEnabled(true);
				}
					break;

				default:
					break;
				}
			}

		});
		btn_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				SharedPreferences.Editor editor = sharedpreferences.edit();
				switch (itemId) {
				case R.id.scan_line:
					editor.putString(AppConstant.PREF_SCANLINE_COLOR,
							mColorList.get(mColorposition));
					editor.commit();
					break;

				case R.id.back_ground:
					editor.putString(AppConstant.PREF_BACK_COLOR,
							mColorList.get(mColorposition));
					editor.commit();
					ColorSingleton.getInstance()
							.setColor(
									sharedpreferences.getString(AppConstant.PREF_BACK_COLOR,
											"#000000"));
					redrawbackground();
					break;
				case R.id.border:
						editor.putString(AppConstant.PREF_BORDER_COLOR,
								mColorList.get(mColorposition));
						editor.commit();
					break;

					default:
					break;

				}
				grdDialog.dismiss();
			}
		});

		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (itemId) {
				case R.id.scan_line:
					String backColor = sharedpreferences.getString(
							AppConstant.PREF_SCANLINE_COLOR, "#000000");
					Horizatal_line.setBackgroundColor(Color
							.parseColor(backColor));
					Verticlel_line.setBackgroundColor(Color
							.parseColor(backColor));

					break;
				case R.id.back_ground: {
					// [START] fix for defect 85
					backColor = sharedpreferences.getString(AppConstant.PREF_BACK_COLOR,
							"#ffFFFF66");
					// [END] fix for defect 85
					LinearLayout ll = (LinearLayout) findViewById(R.id.parent_view);
					ll.setBackgroundColor(Color.parseColor(backColor));
					ColorSingleton.getInstance().setColor(backColor);
					redrawbackground();
				}
					break;
					case R.id.border:
						hexColor = sharedpreferences.getString(
								AppConstant.PREF_BORDER_COLOR, "#ffFFFF66");
						//Toast.makeText(getApplicationContext(),hexColor ,Toast.LENGTH_SHORT).show();

						break;
				default:
					break;
				}

				grdDialog.dismiss();
			}
		});

	}

	protected void redrawbackground() {

		mRedrawRunnable = new Thread(new Runnable() {

			@Override
			public void run() {

				GridView gridView = (GridView) findViewById(R.id.imagegridView);

				mMinPos = 0;
				mMaxPos = gridView.getCount() - 1;
				for (int pos = mMinPos; pos <= mMaxPos; pos++) {

					View thumb = gridView.getChildAt(pos);
					if (thumb != null) {
						mPictureScanHilight = true;
						thumb.setBackgroundColor(Color
								.parseColor(ColorSingleton.getInstance()
										.getColor()));
					}
				}

			}

		});

		mRedrawRunnable.run();

	}


	public static class ColorSingleton {
		private static ColorSingleton singletonInstance = null;
		private String colorString;

		private ColorSingleton() {
			colorString = "#ffffff";
		}

		public static ColorSingleton getInstance() {
			if (singletonInstance == null) {
				singletonInstance = new ColorSingleton();
			}
			return singletonInstance;
		}

		public String getColor() {
			return this.colorString;
		}

		public void setColor(String value) {
			colorString = value;
		}
	}

	// [START] of fix to defect 71
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
			handlerHor.removeCallbacks(mHorizatalLineRunnable);
			handlerHor = null;
		}
		if (handlerVer != null) {
			handlerVer.removeCallbacks(mVerticleLineRunnable);
			handlerVer = null;
		}
		updateUI();
	}// [END] of fix to defect 71

	public static boolean ismReturn_CategoryItemsActivityboolVal() {
		return mReturn_CategoryItemsActivityboolVal;
	}

	public static void setmReturn_CategoryItemsActivityboolVal(
			boolean mReturn_CategoryItemsActivityboolVal) {
		HomeActivity.mReturn_CategoryItemsActivityboolVal = mReturn_CategoryItemsActivityboolVal;
	}

	void showAlert(Context context, String title, String message) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(message)
				.setPositiveButton("OK", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent i = new Intent(HomeActivity.this,
								HomeActivity.class);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						i.putExtra("user",username);
						startActivity(i);
					}
				}).show().setCancelable(false);
	}

	// [START] fix for bug 129
	private boolean appInstalledOrNot(String uri) {
		PackageManager pm = getPackageManager();
		boolean app_installed;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}

	void hideNavigation() {
		runOnUiThread(new Runnable() {
			public void run() {
				if(Build.VERSION.SDK_INT > 13) { // from version 4.0

					if(Build.VERSION.SDK_INT < 19) { //19 or above api
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
	// [END] fix for bug 129
}
