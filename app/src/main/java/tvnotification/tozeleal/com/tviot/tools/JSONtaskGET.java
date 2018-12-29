package tvnotification.tozeleal.com.tviot.tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Class java responsavel pela obtenção de dados JSON
 * @Author António José Velez Leal (8150053)
 * Projeto Final 2017-2018
 * ESTG
 * LEI
 *
 */
public class JSONtaskGET extends AsyncTask<String,String, String> {


    /**
     * Metodo responsavel pelo processo de leitura dos dados JSON de um servidor Home Assistant
     * @param params Lista de parametros com os dados essenciais para a leitura dos dados JSON do servidor Home Assistant
     *               params[0] -> URL
     *               params[1] -> Palavra Chave de acesso
     *
     * @return String de JSON resultante
     * @throws MalformedURLException URL invalido
     * @throws IOException Input invalido
     *
     */
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection =null;

        BufferedReader reader=null;

        try{

            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            String password = params[1];
            if(password!="") {
                connection.setRequestProperty("x-ha-access", password);
                connection.setRequestProperty("Content-Type", "application/json");
            }
            connection.connect();



            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            

            String line="";

            while((line = reader.readLine()) != null){
                buffer.append(line);
            }





            return buffer.toString();



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(connection!=null) {
                connection.disconnect();
            }
            try {
                if(reader!=null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);


    }


    /**
     * Metodo responsavel por fazer parse de um array de valores, utilizado principalmente para fazer parse do parametro "entity_id"
     * @param JSON_all_something String de JSON com varias informações
     * @param entidade Entidade desejada a ser feito o parse
     * @return Um array de IDs
     * @throws JSONException Excepção durante o parse de JSON (JSON invalido?)
     */
    public JSONArray Parse_IDS_Array(String JSON_all_something, String entidade) throws JSONException {

        JSONObject parentObject = new JSONObject(JSON_all_something);
        JSONObject parentObject_temp = parentObject.getJSONObject("attributes");
        JSONArray parentArray = parentObject_temp.getJSONArray(entidade);
        return parentArray;

    }


    /**
     * Metodo responsavel por fazer parse de um valor numérico e transforma para String
     * @param JSON_all_something String de JSON com varias informações
     * @param entidade Entidade desejada a ser feito o parse
     * @return String do valor numerico
     * @throws JSONException Excepção durante o parse de JSON (JSON invalido?)
     */
    public String Parse_IDS_Value(String JSON_all_something, String entidade) throws JSONException {


        JSONObject parentObject = new JSONObject(JSON_all_something);
        String parentFinal =  parentObject.get(entidade).toString();



        return parentFinal;
    }

    /**
     * Metodo responsavel por fazer parse de um valor dentro da estrutura "Attributes"
     * @param JSON_all_something String de JSON com varias informações
     * @param entidade Entidade desejada a ser feito o parse
     * @return String do valor da entidade
     * @throws JSONException Excepção durante o parse de JSON (JSON invalido?)
     */
    public String Parse_IDS_Value_From_Attributes(String JSON_all_something, String entidade) throws JSONException {


        JSONObject parentObject = new JSONObject(JSON_all_something);
        JSONObject parentObject_temp = parentObject.getJSONObject("attributes");
        String parentFinal;

        if(!parentObject_temp.isNull(entidade)) {
            if(entidade=="load_power"){

                Double parentFinalObject = parentObject_temp.getDouble(entidade);
                parentFinal = parentFinalObject.toString();

            }else {
                if (entidade == "in_use") {
                    Double parentFinalObject = parentObject_temp.getDouble(entidade);
                    parentFinal = parentFinalObject.toString();
                } else {
                    parentFinal = (String) parentObject_temp.get(entidade);
                }
            }



        }else {
            parentFinal="null";
        }


        return parentFinal;
    }




}