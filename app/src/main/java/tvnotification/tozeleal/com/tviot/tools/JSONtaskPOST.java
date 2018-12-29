package tvnotification.tozeleal.com.tviot.tools;

import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class java responsavel pelo envio de dados JSON
 * @Author António José Velez Leal (8150053)
 * Projeto Final 2017-2018
 * ESTG
 * LEI
 *
 */

public class JSONtaskPOST  {
    String urlAdress;
    String value;

    /**
     * Metodo construtor de {@link JSONtaskPOST}
     * @param urlAdress URL desejado para o envio da mensagem POST
     * @param value A mensagem a enviar
     */
    public JSONtaskPOST(String urlAdress, String value){
        this.urlAdress=urlAdress;
        this.value=value;
    }

    /**
     * Metodo responsavel pelo envio da mensagem para o Servidor Home Assistant
     */
    public void send() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlAdress);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("entity_id", value);


                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}
