package com.evidencijaclanova;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.evidencijaclanova.R;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class ClanoviFormAdapter extends ArrayAdapter<Grupa> {

  private final List<Grupa> list;
  private final List<Grupa> listaZaClana;
  private final Activity context;
  TextView selectColoursButton;
  String tekst = "";
  public static ArrayList<Integer> selektiraneGrupe = new ArrayList<Integer>();
  public static ArrayList<Integer> obrisaneGrupe = new ArrayList<Integer>();
  
  
  public ClanoviFormAdapter(Activity context, List<Grupa> list, List<Grupa> listaZaClana) {
    super(context, R.layout.grupa_list_item, list);
    this.context = context;
    this.list = list;
    this.listaZaClana = listaZaClana;
  }

  static class ViewHolder {
    protected TextView text;
    protected CheckBox checkbox;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
	  
    View view = null;
 	selectColoursButton = (TextView)context.findViewById(R.id.txtOdabrani);
	
    if (convertView == null)
    {
    	
    //selectedColours = new ArrayList<Integer>();
    //	selectedColours.clear();

      LayoutInflater inflator = context.getLayoutInflater();
      view = inflator.inflate(R.layout.grupa_list_item, null);
      final ViewHolder viewHolder = new ViewHolder();
      viewHolder.text = (TextView) view.findViewById(R.id.label);
      viewHolder.text.setPadding(7, 0, 0, 0);
      viewHolder.text.setTextSize(20);
      viewHolder.text.setWidth(270);
      viewHolder.text.setEllipsize(null);
      viewHolder.text.setSingleLine(false);
      viewHolder.text.setMaxLines(3);
      viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
      viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        
              Grupa element = (Grupa) viewHolder.checkbox.getTag();
              element.setSelected(buttonView.isChecked());
              
              if(buttonView.isChecked())
              {
            	  if(!selektiraneGrupe.contains((Integer) element.get_id()))
            	  {
            			selektiraneGrupe.add((Integer) element.get_id());
        				StringBuilder stringBuilder = new StringBuilder();
        				for(Integer colour : selektiraneGrupe)
        					stringBuilder.append(colour + ",");
        				Log.d("buttonView.isChecked()", stringBuilder.toString());
            	  };

              }
			else
			{
				if(selektiraneGrupe.contains((Integer) element.get_id()))
           	  	{
           				selektiraneGrupe.remove((Integer) element.get_id());
           				StringBuilder stringBuilder = new StringBuilder();
           				for(Integer colour : selektiraneGrupe)
           					stringBuilder.append(colour + ",");
           				Log.d("else", stringBuilder.toString());
       					obrisaneGrupe.add((Integer) element.get_id());
       				
           	  };
           	  
           	  
			}
         	
         	onChangeSelectedColours();
         	
            }
    
          });
  
      view.setTag(viewHolder);
      viewHolder.checkbox.setTag(list.get(position));
    }
    else
    {
      view = convertView;
      ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
       
    }
    ViewHolder holder = (ViewHolder) view.getTag();
    holder.text.setText(list.get(position).get_ime());
    
    	if(listaZaClana != null)
    	{ 
    		if(listaZaClana.size() > 0)
    		{
    			for(Grupa item: listaZaClana)
    			{
    				if(list.get(position).get_idGrupa() ==  item.get_idGrupa())
    				{
    					holder.checkbox.setChecked(true);
    				}
  			
    			}
    		}
    		
    	}
    	
    	else
    	{
    		//holder.checkbox.setChecked(false);
    	}
   
    holder.checkbox.setChecked(list.get(position).isSelected());
    return view;
  }
  
	protected void onChangeSelectedColours() {
		
		StringBuilder stringBuilder = new StringBuilder();
		Collections.sort(selektiraneGrupe);
		for(Integer colour : selektiraneGrupe)
			stringBuilder.append(colour + ",");
		
		selectColoursButton.setText(stringBuilder.toString());
		selectColoursButton.setVisibility(View.INVISIBLE);
	}
	
} 