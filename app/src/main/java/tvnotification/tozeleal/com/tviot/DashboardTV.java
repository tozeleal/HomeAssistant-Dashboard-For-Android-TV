package tvnotification.tozeleal.com.tviot;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import tvnotification.tozeleal.com.tviot.objetos.HassioSensor;
import tvnotification.tozeleal.com.tviot.tools.JSONtaskGET;
import tvnotification.tozeleal.com.tviot.tools.JSONtaskPOST;

/**
 * Class java responsavel pela Dashboard que apresenta todos os dados dos sensores presentes no HomeAssistant
 * Por aqui os Sensores inteligentes são tratados (parse JSON) e instanciados de forma a poder mostrar os dados na UI grafica
 * @Author António José Velez Leal (8150053)
 * Projeto Final 2017-2018
 * ESTG
 * LEI
 *
 */
public class DashboardTV extends AppCompatActivity {

    private String url;
    private String password;
    Handler handler=new Handler();;
    private Boolean gruposFuncionais[];
    protected static final long TIME_DELAY = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_tv);
        String output = null;


        //Obter o url e a password através do intent
        Intent intent = getIntent();
        this.url=intent.getStringExtra("url");
        this.password=intent.getStringExtra("password");
        gruposFuncionais = new Boolean[4];
        try {
            output= new JSONtaskGET().execute(url +"/api/states", "" + password).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(output.contains("\"group.all_switches\"")) {
            HassioSensor[] switchs = inicializador_Switchs();
            inicializador_listas(switchs, R.id.recycleListViewSwitch,LinearLayoutManager.HORIZONTAL);
            gruposFuncionais[0]=true;
        }else{
            gruposFuncionais[0]=false;
        }
        if(output.contains("\"group.all_lights\"")) {
            HassioSensor[] luzes = inicializador_Lights();
            inicializador_listas(luzes, R.id.recycleListViewLights,LinearLayoutManager.HORIZONTAL);
            gruposFuncionais[1]=true;
        }else{
            gruposFuncionais[1]=false;
        }

        if(output.contains("\"group.all_automations\"")){
            HassioSensor[] automation = inicializador_automatizações();
            inicializador_listas(automation, R.id.recycleListViewAutomations,LinearLayoutManager.VERTICAL);
            gruposFuncionais[2]=true;
        }else{
            gruposFuncionais[2]=false;
        }


        if(output.contains("\"group.smartphone_e_tablet\"")) {
            HassioSensor[] android = inicializador_Android();
            inicializador_listas(android, R.id.recycleListViewPhoneAndroid,LinearLayoutManager.HORIZONTAL);
            gruposFuncionais[3]=true;
        }else {
            gruposFuncionais[3]=false;
        }

        handler.post(updateTextRunnable);


    }



    Runnable updateTextRunnable=new Runnable(){
        public void run() {
            if(gruposFuncionais[0]) {
                HassioSensor[] sensor = inicializador_Switchs();
                inicializador_listas(sensor, R.id.recycleListViewSwitch,LinearLayoutManager.HORIZONTAL);
            }
            if(gruposFuncionais[1]) {
                HassioSensor[] luzes = inicializador_Lights();
                inicializador_listas(luzes, R.id.recycleListViewLights,LinearLayoutManager.HORIZONTAL);
            }
            if(gruposFuncionais[2]){
                HassioSensor[] automation = inicializador_automatizações();
                inicializador_listas(automation, R.id.recycleListViewAutomations,LinearLayoutManager.VERTICAL);
            }

            if(gruposFuncionais[3]){
                HassioSensor[] android = inicializador_Android();
                inicializador_listas(android, R.id.recycleListViewPhoneAndroid,LinearLayoutManager.HORIZONTAL);
            }


            handler.postDelayed(this, TIME_DELAY);
        }
    };

    /**
     * Metodo Responsavel por instanciar um Array de {@link HassioSensor} que corresponde a um array de luzes inteligentes
     * @return  Um array de luzes inteligentes
     */
    public HassioSensor[] inicializador_Lights(){



        JSONtaskGET jsoNtaskGET = new JSONtaskGET();
        String output_Switch;
        HassioSensor[] sensortest;

        try {

            output_Switch = jsoNtaskGET.execute(this.url +"/api/states/group.all_lights", "" +  this.password).get();
            JSONArray array = jsoNtaskGET.Parse_IDS_Array(output_Switch, "entity_id");
            sensortest=new HassioSensor[array.length()];
            for(int i=0;i<array.length();i++) {
                Log.d("Output-test-debug", array.getString(i));
                Log.d("Output-test-debug", this.url + "/api/states/" + array.getString(i));
                sensortest[i] = new HassioSensor();
                String switch1 = new JSONtaskGET().execute(this.url + "/api/states/" + array.getString(i), "" + this.password).get();

                sensortest[i].setFriendlyName(new JSONtaskGET().Parse_IDS_Value_From_Attributes(switch1, "friendly_name"));
                sensortest[i].setId(new JSONtaskGET().Parse_IDS_Value(switch1, "entity_id"));
                sensortest[i].setState(new JSONtaskGET().Parse_IDS_Value(switch1, "state"));

                sensortest[i].setInUse("0");
                sensortest[i].setType(1);


            }
            return sensortest;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Metodo Responsavel por instanciar um Array de {@link HassioSensor} que corresponde a um array de Scripts de Automação
     * @return  Um array de luzes inteligentes
     */
    public HassioSensor[] inicializador_automatizações(){



        JSONtaskGET jsoNtaskGET = new JSONtaskGET();
        String output_Switch;
        HassioSensor[] sensortest;

        try {

            output_Switch = jsoNtaskGET.execute(this.url +"/api/states/group.all_automations", "" +  this.password).get();
            JSONArray array = jsoNtaskGET.Parse_IDS_Array(output_Switch, "entity_id");
            sensortest=new HassioSensor[array.length()];
            for(int i=0;i<array.length();i++) {
                Log.d("Output-test-debug", array.getString(i));
                Log.d("Output-test-debug", this.url + "/api/states/" + array.getString(i));
                sensortest[i] = new HassioSensor();
                String switch1 = new JSONtaskGET().execute(this.url + "/api/states/" + array.getString(i), "" + this.password).get();

                sensortest[i].setFriendlyName(new JSONtaskGET().Parse_IDS_Value_From_Attributes("" +switch1, "friendly_name"));
                sensortest[i].setId(new JSONtaskGET().Parse_IDS_Value("" +switch1, "entity_id"));
                sensortest[i].setState(new JSONtaskGET().Parse_IDS_Value(""+ switch1, "state"));
                sensortest[i].setInUse("0");
                sensortest[i].setType(2);


            }
            return sensortest;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Metodo Responsavel por instanciar um Array de {@link HassioSensor} que corresponde a um array de dispositivos Android
     * @return Um array de dispositivos Android
     */
    public HassioSensor[] inicializador_Android(){



        JSONtaskGET jsoNtaskGET = new JSONtaskGET();
        String output_Switch;
        HassioSensor[] sensortest;

        try {

            output_Switch = jsoNtaskGET.execute(this.url +"/api/states/group.smartphone_e_tablet", "" +  this.password).get();
            JSONArray array = jsoNtaskGET.Parse_IDS_Array(output_Switch, "entity_id");
            sensortest=new HassioSensor[array.length()];
            for(int i=0;i<array.length();i++) {
                Log.d("Output-test-debug", array.getString(i));
                Log.d("Output-test-debug", this.url + "/api/states/" + array.getString(i));
                sensortest[i] = new HassioSensor();
                String switch1 = new JSONtaskGET().execute(this.url + "/api/states/" + array.getString(i), "" + this.password).get();

                sensortest[i].setFriendlyName(new JSONtaskGET().Parse_IDS_Value_From_Attributes(switch1, "friendly_name"));
                sensortest[i].setId(new JSONtaskGET().Parse_IDS_Value(switch1, "entity_id"));
                sensortest[i].setState(new JSONtaskGET().Parse_IDS_Value(switch1, "state"));

                sensortest[i].setType(3);


            }
            return sensortest;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metodo Responsavel por instanciar um Array de {@link HassioSensor} que corresponde a um array de fichas inteligentes
     * @return Um array de Fichas inteligentes
     */
    public HassioSensor[] inicializador_Switchs(){

        //Obter o url e a password através do intent
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String password = intent.getStringExtra("password");
        this.url=url;
        this.password=password;

        JSONtaskGET jsoNtaskGET = new JSONtaskGET();
        String output_Switch = null;
        HassioSensor[] sensortest;

        try {

            output_Switch = jsoNtaskGET.execute(this.url +"/api/states/group.all_switches", "" +  this.password).get();
            JSONArray array = jsoNtaskGET.Parse_IDS_Array(output_Switch, "entity_id");
            sensortest=new HassioSensor[array.length()];
            for(int i=0;i<array.length();i++) {
                Log.d("Output-test-debug", array.getString(i));
                Log.d("Output-test-debug", this.url + "/api/states/" + array.getString(i));
                sensortest[i] = new HassioSensor();
                String switch1 = new JSONtaskGET().execute(this.url + "/api/states/" + array.getString(i), "" + this.password).get();

                sensortest[i].setFriendlyName(new JSONtaskGET().Parse_IDS_Value_From_Attributes(switch1, "friendly_name"));
                sensortest[i].setId(new JSONtaskGET().Parse_IDS_Value(switch1, "entity_id"));
                sensortest[i].setPowerConsumed(new JSONtaskGET().Parse_IDS_Value_From_Attributes(switch1, "load_power"));
                if (sensortest[i].getPowerConsumed().contains("null")) {
                    sensortest[i].setPowerConsumed("0.0");
                }
                sensortest[i].setState(new JSONtaskGET().Parse_IDS_Value(switch1, "state"));
                sensortest[i].setInUse(new JSONtaskGET().Parse_IDS_Value_From_Attributes(switch1, "in_use"));
                sensortest[i].setType(0);


            }
            return sensortest;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metodo Responsavel por instanciar uma lista de sensores
     * @return Lista de sensores
     */
    public void inicializador_listas(HassioSensor[] sensor, int viewId, int orientation){




        ArrayList<HassioSensor> sensores = new ArrayList<>();
        for(int i=0;i<sensor.length;i++){
            sensores.add(sensor[i]);
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, orientation, false);
        RecyclerView recyclerView = findViewById(viewId);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,sensores,this.url);
        recyclerView.setAdapter(adapter);
    }


    /**
     * Metodo responsavel por ligar todas as fichas inteligentes presentes no sistema Home Assistant
     * @param view
     */
    public void turn_on_all_switch(View view){

        new JSONtaskPOST(url+"/api/services/switch/turn_on", "group.all_switches").send();

    }
    /**
     * Metodo responsavel por desligar todas as fichas inteligentes presentes no sistema Home Assistant
     * @param view
     */
    public void turn_off_all_switch(View view){
        new JSONtaskPOST(url+"/api/services/switch/turn_off", "group.all_switches").send();
    }
    /**
     * Metodo responsavel por ligar todas as lampadas inteligentes presentes no sistema Home Assistant
     * @param view
     */
    public void turn_on_all_light(View view){
        new JSONtaskPOST(url+"/api/services/light/turn_on", "group.all_lights").send();
    }
    /**
     * Metodo responsavel por desligar todas as lampadas inteligentes presentes no sistema Home Assistant
     * @param view
     */
    public void turn_off_all_light(View view){
        new JSONtaskPOST(url+"/api/services/light/turn_off", "group.all_lights").send();
    }



}
