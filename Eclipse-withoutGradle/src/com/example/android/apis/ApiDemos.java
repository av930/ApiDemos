/*
 * KJK_TALK APIDEMOS: Entry Point 

 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.apis;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.Collator;
import java.text.DecimalFormat;
import java.util.*;

/**
 * KJK_TALK: List Menu를 만들어내는 Activity
 *
 */
public class ApiDemos extends ListActivity {
	static final Boolean useManifestVal=true;
	
	/** 
	 * 화면에 list menu를 만들어 뿌려준다.
	 * 만약 지정된 path가 있다면 그 path를 기준으로 list menu를 만든다.  
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		// 아래 browseIntent에서 넣어준 값이 있으면 읽어와서
		String path = intent.getStringExtra("com.example.android.apis.Path");

		if (path == null) {
			path = "";
		}

		setListAdapter(new SimpleAdapter(this, getData(path),
				R.layout.listofsmallheight, new String[] { "title" },
				new int[] { android.R.id.text1 }));
		getListView().setTextFilterEnabled(true);
	}

	
	
	/** 
	 * 현재 dir를 기준으로 list에 출력된 data와 그 data가 호출할 intent를 얻어오는 역활을 한다.
	 */
	protected List<Map<String, Object>> getData(String prefix) {

		String act_delimter = "/";
		if (useManifestVal==false)  act_delimter = ".";

		//final String act_delimter = "/";
		// KJK_TALK: Map<String, Object:>을 item으로 가지는 ArrayList생성
		List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();

		// intent를 하나 만들고, apidemos가 속한 sample category를 설정한후
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_SAMPLE_CODE);

		PackageManager pm = getPackageManager();
		// 위에서 만들어진 intent로 호출될수 있는 모든 activity list를 얻어와
		List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);

		if (null == list)
			return myData;

		String[] prefixPath;
		String prefixWithSlash = prefix;

		if (prefix.equals("")) { // 맨처음 menu list를 만들때
			prefixPath = null;
		} else { //두번재 depth이상 menu list를 만들때
			prefixPath = prefix.split("\\" + act_delimter); // 현재 dir 아래에서만
															// list를 구성하도록 한다.
			prefixWithSlash = prefix + act_delimter;
		}

		// 얻어온 activity list의 item 갯수를 세어, 현재 199개
		int len = list.size();

		
		// <string, boolean>을 item으로 갖는 HashMap 생성, path list를 관리하기 위해 set으로 하지..
		Collection<String> entries = new HashSet<String>();

		for (int i = 0; i < len; i++) {
			// list의 첫번째 act를 가져와서 info를 추출.
			ResolveInfo info = list.get(i);
			CharSequence labelSeq = info.loadLabel(pm);
			// title 이름을 정하고.
			String label;
			if (useManifestVal) //AndroidManifest를 이용하는 방법
				label= labelSeq != null ? labelSeq.toString() : info.activityInfo.name;
			else  //그냥 package명을 이용하는 방법
				label= info.activityInfo.name.substring("com.example.android.apis.".length());

			// 마지막 leaf node activity list가 아닐때라면
			if (prefixWithSlash.length() == 0|| label.startsWith(prefixWithSlash)) {
				// App/Activity/Hello World
				String[] labelPath = label.split("\\" + act_delimter);
				// App, Activity, Hello World
				String nextLabel = prefixPath == null ? labelPath[0]: labelPath[prefixPath.length];
				String nextLabelPath=nextLabel;
				//if ("app".equals(nextLabel)) nextLabelPath="A."+nextLabel;

				
				// 마지막 leaf node인경우, 즉 act를 호출하는 경우
				if ((prefixPath != null ? prefixPath.length : 0) == labelPath.length - 1) {


					try {
              		    String className = info.activityInfo.name;
						//class이름으로 class를 얻어와
						Class <?> clazz = Class.forName(className);
						//annotation이 존재한다며, 그 annotation type으로 읽어와 
						//sequence 번호를 뿌려준다.
						if (clazz.isAnnotationPresent(ActivityIsReviewed.class)) {
							ActivityIsReviewed clazzAnnot = clazz
									.getAnnotation(ActivityIsReviewed.class);
							DecimalFormat df = new DecimalFormat("00.00 ");
							nextLabel = df.format(clazzAnnot.sequence())
									+ nextLabel;
						}

					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					addItem(myData,
							nextLabel,
							activityIntent(
									info.activityInfo.applicationInfo.packageName,
									info.activityInfo.name));
					
				// activity가 아닌 category를 호출하는 경우	
				} else {
					if (entries.contains(nextLabel)==false){
						// App와 같은 처음에 오는 dir일 경우
						// App, Media, OS, Contents, Graphics, Text, View .. 등의
						// 첫번째 dir과
						// 그것의 sub Menu Act를 호출할수 있는 intent를 만들어서 myData에 같이
						// 넣는다.
						// 이때 enries에도 기록하여 해당 path가 이미 기록된것인지 판단하는데 사용한다.

						addItem(myData, 
								nextLabelPath,
								browseIntent(prefix.equals("") ? nextLabel
										: prefix + act_delimter + nextLabel));
						entries.add(nextLabel);
					}
				}
			}
		}

		// Collection의 sub class인 list myData를 ordering한다.
		Collections.sort(myData, sDisplayNameComparator);

		return myData;
	}

	private final static Comparator<Map<String, Object>> sDisplayNameComparator = new Comparator<Map<String, Object>>() {
		private final Collator collator = Collator.getInstance();

		// HashMap->HashMap$Entry.<String, Object>구조에서
		// HashMap->HashMap$Entry.<title, dir>
		// 을 가지고 정렬을 시도한다. compare는 결국 문자열을 비교한다.
		public int compare(Map<String, Object> map1, Map<String, Object> map2) {
			/*
			 * StringBuffer s1 = new StringBuffer(map1.get("title").toString());
			 * s1.reverse(); StringBuffer s2 = new
			 * StringBuffer(map2.get("title").toString()); s2.reverse();
			 * 
			 * return collator.compare(s2.toString(), s1.toString());
			 */
			return collator.compare(map1.get("title"), map2.get("title"));
		}
	};

	// leaf node인경우 마지막 호출할 act정보를 기록한다.
	protected Intent activityIntent(String pkg, String componentName) {
		Intent result = new Intent();
		result.setClassName(pkg, componentName);
		return result;
	}

	// leaf node가 아닌경우 ApiDemos를 이용하여 list를 출력하는데,기존점인 dir를 넘겨
	// list가 어디서 부터 출력할지 알려준다.
	protected Intent browseIntent(String path) {
		Intent result = new Intent();
		result.setClass(this, ApiDemos.class);
		result.putExtra("com.example.android.apis.Path", path);
		return result;
	}

	// Map<String, Object:>을 item으로 가지는 ArrayList --> myData
	// 들어갈 Dir name(ex, App,Contents...) --> name
	// ApiDemos Activity(자기자신을)를 호출할 intent
	// --> dir sub menu를 보여줄 act로 최종은 실제 activity가 된다.
	protected void addItem(List<Map<String, Object>> data, String name, Intent intent) {
		Map<String, Object> temp = new HashMap<String, Object>();
		// temp.put에서는 temp에다가 <string,Object>로 된 item을 만들어서 put해준다.
		// temp는 항상 Map<string, ?> 로 된 format이어야 한다.
		temp.put("title", name);
		// 실제 temp의 item으로 들어가는 <String, Object>는 temp.put method에서 new해준다.
		// ArrayList->HashMap->HashMap$Entry.<String, Object>
		// reference->dir이름->sub dir이름, sub dir은 menu act가 될수도 있고, 최종 act가 될수도
		// 있다
		temp.put("intent", intent);
		data.add(temp);
	}

	// 현재 menu를 보여주는 list act에서 해당 item을 click했을때 호출
	@Override
	@SuppressWarnings("unchecked")
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Map<String, Object> map = (Map<String, Object>) l.getItemAtPosition(position);
		// reference->dir이름->sub dir이름, sub dir은 menu act가 될수도 있고, 최종 act가 될수도있다
		// 그러므로 최종 act면 최종 act를 시작시키고, listact를 상속받은 menu act면 menu act를 생성한다.
		// 만약 menu act면 현재 class이므로 oncreate에서 list menu를 draw해주게 되고,
		// 최종 act면 각 act의 oncreate를 실행하게 된다.

        Intent intent = new Intent((Intent) map.get("intent"));
        intent.addCategory(Intent.CATEGORY_SAMPLE_CODE);
        startActivity(intent);
    }
}
