/*
*Copyright 2011 Matthieu Paret
*
*This file is part of DragAndDrop.
*
*DragAndDrop is free software: you can redistribute it and/or modify
*it under the terms of the GNU Lesser General Public License as published by
*the Free Software Foundation, either version 3 of the License, or
*(at your option) any later version.
*
*DragAndDrop is distributed in the hope that it will be useful,
*but WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*GNU General Public License for more details.
*
*You should have received a copy of the GNU Lesser General Public License
*along with DragAndDrop.  If not, see <http://www.gnu.org/licenses/>.
*/

package exp.mtparet.dragdrop;

import java.util.ArrayList;
import java.util.Locale;

import exp.mtparet.dragdrop.adapter.ItemAdapter;
import exp.mtparet.dragdrop.adapter.ReceverAdapter;
import exp.mtparet.dragdrop.data.OneItem;
import exp.mtparet.dragdrop.view.HorizontalListView;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class DragAndDropHorizontal extends Activity {
    
	private RelativeLayout mainRelativeLayout;
	private HorizontalListView lvPicture;
	private HorizontalListView lvRecever;
	private ItemAdapter itemAdapter;
	private ReceverAdapter receverAdapter;
	private ImageView imageDrag;
	private OneItem oneItemSelected;
	private Context context;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        context = this;
        
        mainRelativeLayout = (RelativeLayout)RelativeLayout.inflate(context, R.layout.main_horizontal, null);
        lvPicture = (HorizontalListView)mainRelativeLayout.findViewById(R.id.listView1);
		lvRecever = (HorizontalListView)mainRelativeLayout.findViewById(R.id.listView2);
		imageDrag = (ImageView)mainRelativeLayout.findViewById(R.id.imageView1);
		
		itemAdapter = new ItemAdapter(context, constructList());
		receverAdapter = new ReceverAdapter(context, new ArrayList<OneItem>());
		
		lvPicture.setAdapter(itemAdapter);
		lvRecever.setAdapter(receverAdapter);
		
		
		lvPicture.setOnItemSelectedListener(mOnItemSelectedListener);
		lvPicture.setOnItemMoveListener(mOnItemMoveListener);
		lvPicture.setOnItemUpOutListener(mOnItemUpOutListener);
		
		lvRecever.setOnItemReceiverListener(listenerReceivePicture);
		lvRecever.setOnItemLongClickListener(listenerRemoveItem);
		
		setContentView(mainRelativeLayout);
    }
    
    /**
     * Save selected item
     */
    private AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			/**
			 * retrieve selected item from adapterview
			 */
			oneItemSelected = (OneItem) arg0.getItemAtPosition(arg2);
			imageDrag.setImageDrawable(context.getResources().getDrawable(oneItemSelected.getId()));
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}

	};
	
	private OnTouchListener mOnItemUpOutListener = new  OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
		
		return lvRecever.onTouchEvent(event);
		}
	};


	private OnItemClickListener listenerReceivePicture = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if(oneItemSelected != null)
				receverAdapter.addPicture(oneItemSelected, arg2);

		}

	};

	private OnItemLongClickListener listenerRemoveItem = new OnItemLongClickListener() {


		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			receverAdapter.removeItem(arg2);
			return false;
		}

	};



	private OnTouchListener mOnItemMoveListener = new  OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) imageDrag.getLayoutParams();
			imageDrag.setVisibility(ImageView.VISIBLE);
			
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				imageDrag.bringToFront();
				return true;
			}
			
			if (event.getAction()==MotionEvent.ACTION_MOVE) {
				layout.leftMargin = (int) event.getX() - imageDrag.getWidth()/2;    		
				layout.topMargin = (int) event.getY() - imageDrag.getHeight()/2;
			}
			
			if (event.getAction()==MotionEvent.ACTION_UP) {
				imageDrag.setVisibility(View.GONE);
			}
			
			imageDrag.setLayoutParams(layout);
			
			return true;
		}

	};
	
	private ArrayList<OneItem> constructList(){
		ArrayList<OneItem> al = new ArrayList<OneItem>();
		
		OneItem op = new OneItem(R.drawable.maison, "maison");
		al.add(op);
		
		OneItem op2 = new OneItem(R.drawable.dans, "dans");
		al.add(op2);
		
		OneItem op3 = new OneItem(R.drawable.dort, "dort");
		al.add(op3);
		
		OneItem op4 = new OneItem(R.drawable.garcon, "gar�on");
		al.add(op4);
		
		OneItem op5 = new OneItem(R.drawable.le, "le");
		al.add(op5);
	
	return al;
	}

}