package com.imglow.ElementMMO;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.sun.media.jfxmedia.events.PlayerStateEvent.PlayerState;


public class TextureSingleton {
	private static TextureSingleton instance;
	//////////////DONT TOUCH////////////////
	public static int COP = 0, SPIKEY = 1, CHIKA = 2, NAKED_MAN = 3;
	public static int STAND = 0, FACE_UP = 1, FACE_LEFT = 2, WALK_DOWN = 3, WALK_UP = 4, WALK_LEFT_1 = 5,
						WALK_LEFT_2 = 6, WALK_RIGHT_1 = 7, WALK_RIGHT_2 = 8, FACE_RIGHT = 9, WALK_DOWN_2 = 10, WALK_UP_2 = 11;
	
			//playersprites.get(COP).get(UP);
	public ArrayList<ArrayList<TextureRegion>> playerSprites;
	
	public Texture white, gray;
	public BitmapFont scoreFont, nameFont, instructionFont;
	public Texture cop, spikey, chika, nakedMan;
	public ArrayList<TextureRegion> copList, spikeyList, chikaList, nakedManList, shopElements, elements, grassArray, storeArray;
	public TextureRegion grass, whiteGrass, blackGrass, bush, tree1, tree2, imagination, hunger, music, giggles, segFaults, sunshine, drought, entropy, 
						trig, choco, synergy, hugs, shop, whiteRegion, grayRegion, battle, go, goGrayed, vs, fullHeart,halfHeart, noHeart, storeCorner, 
						storeEmpty, storeFlowers, storeSide, storeStall1, storeStall2, storePot, nothing, msgBox, win, tie, lose, victory, defeat, spot,
						yellow, black;
	
	public Music mainMusic;
	public Music battleMusic;
	public Sound cannotAccess;
	
	protected TextureSingleton(){}

	public static TextureSingleton getInstance() {
		if(instance == null) {
			instance = new TextureSingleton();
		}
		return instance;
	}
	
	public void loadTextures()
	{
		//This is where textures are loaded
		white = new Texture(Gdx.files.internal("images/white.png"));
		gray = new Texture(Gdx.files.internal("images/gray.png"));
		yellow = new TextureRegion(new Texture(Gdx.files.internal("images/yellow.png")));
		black = new TextureRegion(new Texture(Gdx.files.internal("images/black.png")));
		playerSprites = new ArrayList<ArrayList<TextureRegion>>();
		scoreFont = new BitmapFont(Gdx.files.internal("fonts/Fipps-Regular.fnt"), Gdx.files.internal("fonts/Fipps-Regular_0.tga"), false);
		nameFont = new BitmapFont(Gdx.files.internal("fonts/FlxRegular.fnt"), Gdx.files.internal("fonts/FlxRegular_0.tga"), false);
		instructionFont = new BitmapFont(Gdx.files.internal("fonts/Fipps-Regular.fnt"), Gdx.files.internal("fonts/Fipps-Regular_0.tga"), false);
		whiteRegion = new TextureRegion(white);
		grayRegion = new TextureRegion(gray);
		battle = new TextureRegion(new Texture(Gdx.files.internal("images/Battle.png")));
		goGrayed = new TextureRegion(new Texture(Gdx.files.internal("images/GoGrayed.png")));
		go = new TextureRegion(new Texture(Gdx.files.internal("images/Go.png")));
		vs = new TextureRegion(new Texture(Gdx.files.internal("images/Vs.png")));
		fullHeart = new TextureRegion(new Texture(Gdx.files.internal("images/full_heart.png")));
		halfHeart = new TextureRegion(new Texture(Gdx.files.internal("images/half_heart.png")));
		noHeart = new TextureRegion(new Texture(Gdx.files.internal("images/no_heart.png")));
		nothing = new TextureRegion(new Texture(Gdx.files.internal("images/nothing.png")));
		msgBox = new TextureRegion(new Texture(Gdx.files.internal("images/msgbox.png")));
		 win = new TextureRegion(new Texture(Gdx.files.internal("images/win.png")));
		tie = new TextureRegion(new Texture(Gdx.files.internal("images/tie.png")));
		lose = new TextureRegion(new Texture(Gdx.files.internal("images/lose.png")));
		// victory = new TextureRegion(new Texture(Gdx.files.internal("images/victory.png")));
		defeat = new TextureRegion(new Texture(Gdx.files.internal("images/defeat.png")));
		
		spot = new TextureRegion(new Texture(Gdx.files.internal("images/spot.png")));
		cop = new Texture(Gdx.files.internal("images/cop.png"));
		spikey = new Texture(Gdx.files.internal("images/Spikey.png"));
		chika = new Texture(Gdx.files.internal("images/chika.png"));
		nakedMan = new Texture(Gdx.files.internal("images/naked_man.png"));
		nakedManList = new ArrayList<TextureRegion>();	
		copList = new ArrayList<TextureRegion>();
		spikeyList = new ArrayList<TextureRegion>();
		chikaList = new ArrayList<TextureRegion>();
		
		playerSprites.add(copList);
		playerSprites.add(spikeyList);
		playerSprites.add(chikaList);	
		playerSprites.add(nakedManList);
		
		Texture temp = new Texture(Gdx.files.internal("images/grass.png"));		//temp is a whore for initializing stuff
		//I'm going green now.
		grass = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/whitegrass.png"));
		whiteGrass = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/blackgrass.png"));
		blackGrass = new TextureRegion(temp);
		
		temp = new Texture(Gdx.files.internal("images/storeCorner.png"));
		storeCorner = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/storeEmpty.png"));
		storeEmpty = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/storeFlowers.png"));
		storeFlowers = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/storeSide.png"));
		storeSide = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/storeStall1.png"));
		storeStall1 = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/storeStall2.png"));
		storeStall2 = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/storePot.png"));
		storePot = new TextureRegion(temp);
		storeArray = new ArrayList<TextureRegion>();
		storeArray.add(storeEmpty);
		storeArray.add(storeEmpty);
		storeArray.add(storeEmpty);
		storeArray.add(storeFlowers);
		storeArray.add(storeStall1);
		storeArray.add(storeStall2);
		storeArray.add(storePot);
		
		temp = new Texture(Gdx.files.internal("images/bush.png"));
		bush = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/tree1.png"));
		tree1 = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/tree2.png"));
		tree2 = new TextureRegion(temp);
		grassArray = new ArrayList<TextureRegion>();
		grassArray.add(grass);
		grassArray.add(whiteGrass);
		grassArray.add(blackGrass);
		//Done with that green shit. Not I create people for I am the CREATOR!!!!
		for(int i = 0; i < 12; i++) {
			copList.add(new TextureRegion(cop, i * 16, 0, 16, 21));
		}
		for(int i = 0; i < 12; i++) {
			spikeyList.add(new TextureRegion(spikey, i * 16, 0, 16, 21));
		}
		for(int i = 0; i < 12; i++) {
			chikaList.add(new TextureRegion(chika, i * 16, 0, 16, 21));
		}
		for(int i = 0; i < 12; i++) {
			nakedManList.add(new TextureRegion(nakedMan, i * 16, 0, 16, 21));
		}
		
		////Background Music shit///
		mainMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/background_music.mp3"));
		mainMusic.setLooping(true);
		mainMusic.play();
		
		////Battle music shit////
		battleMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/battle.mp3"));
		
		
		////More music shit////
		cannotAccess = Gdx.audio.newSound(Gdx.files.internal("sound/accessDenied.mp3"));
		
		// Adding elements to shop ArrayList & initalizing shop//
		temp = new Texture(Gdx.files.internal("images/Shop/Imagination.png"));
		imagination = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/Shop/Giggles.png"));
		giggles = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/Shop/SegFaults.png"));
		segFaults = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/Shop/Sunshine.png"));
		sunshine = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/Shop/Music.png"));
		music = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/Shop/Hunger.png"));
		hunger = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/Shop/Drought.png"));
		drought = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/Shop/Entropy.png"));
		entropy = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/Shop/Trigonometry.png"));
		trig = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/Shop/Chocolate.png"));
		choco = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/Shop/Synergy.png"));
		synergy = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/Shop/Hugs.png"));
		hugs = new TextureRegion(temp);
		
		temp = new Texture(Gdx.files.internal("images/Shop/Shoppe.png"));
		shop = new TextureRegion(temp);
		
		
		shopElements = new ArrayList<TextureRegion>();
		shopElements.add(imagination);
		shopElements.add(giggles);
		shopElements.add(segFaults);
		shopElements.add(sunshine);
		shopElements.add(hunger);
		shopElements.add(music);
		shopElements.add(drought);
		shopElements.add(entropy);
		shopElements.add(trig);
		shopElements.add(choco);
		shopElements.add(synergy);
		shopElements.add(hugs);
		
		//Adding elements in main Arraylist//
		temp =  new Texture(Gdx.files.internal("images/Elements/Imagination.png"));
		imagination = new TextureRegion(temp);
		temp =  new Texture(Gdx.files.internal("images/Elements/giggles.png"));
		giggles = new TextureRegion(temp);
		temp =  new Texture(Gdx.files.internal("images/Elements/SegFaults.png"));
		segFaults = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/Elements/Hunger.png"));
		hunger = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/Elements/Music.png"));
		music = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/Elements/Sunshine.png"));
		sunshine = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/Elements/Drought.png"));
		drought = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/Elements/Entropy.png"));
		entropy = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/Elements/Trigonometry.png"));
		trig = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/Elements/Chocolate.png"));
		choco = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/Elements/Synergy.png"));
		synergy = new TextureRegion(temp);
		temp = new Texture(Gdx.files.internal("images/Elements/Hugs.png"));
		hugs = new TextureRegion(temp);
		
		elements = new ArrayList<TextureRegion>();
		elements.add(imagination);
		elements.add(giggles);
		elements.add(segFaults);
		elements.add(sunshine);
		elements.add(hunger);
		elements.add(music);
		elements.add(drought);
		elements.add(entropy);
		elements.add(trig);
		elements.add(choco);
		elements.add(synergy);
		elements.add(hugs);
	}
	
	public void EnterBattle() {			//Changing music when entering the battle.
		mainMusic.pause();
		battleMusic.setLooping(true);
		battleMusic.play();
	}
	
	public void ExitBattle() {			//Changing music when exiting the battle.
		battleMusic.pause();
		mainMusic.play();
	}
	
	public void playAccessDenied() {
		cannotAccess.play();
	}
	
}
