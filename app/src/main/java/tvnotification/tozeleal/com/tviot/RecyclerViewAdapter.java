package tvnotification.tozeleal.com.tviot;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import tvnotification.tozeleal.com.tviot.objetos.HassioSensor;
import tvnotification.tozeleal.com.tviot.tools.JSONtaskPOST;
/**
 * Class java responsavel pela apresentação dos dados de um sensor intelgente na view enviada por argumento
 * @Author António José Velez Leal (8150053)
 * Projeto Final 2017-2018
 * ESTG
 * LEI
 *
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<HassioSensor> sensores;
    private Context mContext;
    private String url;

    /**
     * Metodo construtor do {@link RecyclerViewAdapter}
     * @param context Ligação á view original
     * @param sensores Array de Sensores
     * @param url URL do servidor Home Assistant
     */
    public RecyclerViewAdapter(Context context, ArrayList sensores, String url) {
        this.mContext = context;
        this.sensores = sensores;
        this.url = url;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.butao_luz, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Metodo responsavel pelo momento de preenchimento de informação na view do sensor
     * @param holder Conjunto de referencias a View butao_luz
     * @param position Posição do array relativo ao sensor inteligente
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textViewTitulo.setText(sensores.get(position).getFriendlyName());
        if(sensores.get(position).getState().contains("unknown")){
            holder.fundo.setVisibility(View.GONE);
            return;
        }

        if(sensores.get(position).getState().contains("on")){
            holder.button.setChecked(true);
            holder.textViewConsumo.setText(sensores.get(position).getPowerConsumed()+"W");
            if(sensores.get(position).getInUse().contains("1") && sensores.get(position).getType() == 0) {
                holder.fundo.setBackgroundColor(Color.YELLOW);
            }

        }else{
            holder.button.setChecked(false);
            holder.textViewConsumo.setText("0.0W");

        }
        if(sensores.get(position).getType()==0){

        }else{
            holder.textViewSubtitulo.setVisibility (View.GONE);

            if(sensores.get(position).getType()==3){
                holder.button.setText("");
                holder.button.setClickable(false);





                if(sensores.get(position).getState().length()==0){
                    holder.textViewConsumo.setText("-- %");
                }else {
                    holder.textViewConsumo.setText(sensores.get(position).getState()+"%");
                    if (Integer.parseInt(sensores.get(position).getState()) < 15) {
                        holder.fundo.setBackgroundColor(Color.YELLOW);
                    }
                }




            }else{
                holder.textViewConsumo.setVisibility (View.GONE);

            }
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch
                if(sensores.get(position).getType()==0) {
                    if (sensores.get(position).getState().contains("on")) {
                        new JSONtaskPOST(url + "/api/services/switch/turn_off", sensores.get(position).getId()).send();
                        sensores.get(position).setState("off");
                    } else {
                        new JSONtaskPOST(url + "/api/services/switch/turn_on", sensores.get(position).getId()).send();
                        sensores.get(position).setState("on");
                    }
                }
                //Light
                if(sensores.get(position).getType()==1) {
                    if (sensores.get(position).getState().contains("on")) {
                        new JSONtaskPOST(url + "/api/services/light/turn_off", sensores.get(position).getId()).send();
                        sensores.get(position).setState("off");
                    } else {
                        new JSONtaskPOST(url + "/api/services/light/turn_on", sensores.get(position).getId()).send();
                        sensores.get(position).setState("on");
                    }
                }
                if(sensores.get(position).getType()==2) {
                    if (sensores.get(position).getState().contains("on")) {
                        new JSONtaskPOST(url + "/api/services/automation/turn_off", sensores.get(position).getId()).send();
                        sensores.get(position).setState("off");
                    } else {
                        new JSONtaskPOST(url + "/api/services/automation/turn_on", sensores.get(position).getId()).send();
                        sensores.get(position).setState("on");
                    }
                }


                Log.d("Output", "Funciona");
            }
        });

    }

    /**
     * Metodo responsavel por saber o tamanho do array de sensores
     * @return Tamanho do array
     */
    @Override
    public int getItemCount() {
        return sensores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitulo;
        ToggleButton button;
        TextView textViewSubtitulo;
        TextView textViewConsumo;
        LinearLayout fundo;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.textViewName);
            button = itemView.findViewById(R.id.toggleButton2);
            textViewSubtitulo = itemView.findViewById(R.id.textViewVersion);
            textViewConsumo = itemView.findViewById(R.id.textViewConsumo);
            fundo = itemView.findViewById(R.id.linearBotaoSensor);

        }
    }


}
