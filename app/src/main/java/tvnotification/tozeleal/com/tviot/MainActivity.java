package tvnotification.tozeleal.com.tviot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import java.util.concurrent.ExecutionException;

import tvnotification.tozeleal.com.tviot.tools.JSONtaskGET;

/**
 * Class java responsavel pelo login e ligação ao servidor HomeAssistant
 * @Author António José Velez Leal (8150053)
 * Projeto Final 2017-2018
 * ESTG
 * LEI
 *
 */
public class MainActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText ip = findViewById(R.id.editText);
        final EditText password = findViewById(R.id.editText2);
        final EditText port = findViewById(R.id.editText3);
        final CheckBox caixaHttps = findViewById(R.id.checkBox);


        Log.d("Output-test-debug","I am here");
        //Leitura dos dados anteriores de login
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mPreferences = getSharedPreferences("DadosLogin", Context.MODE_PRIVATE);
        mEditor=mPreferences.edit();
        ip.setText(mPreferences.getString("ip",""));
        password.setText(mPreferences.getString("password", ""));
        port.setText(mPreferences.getString("port","8123"));
        caixaHttps.setChecked(mPreferences.getBoolean("https",false));


    }

    //---- FUNÇÕES DOS BOTÕES -----


    /**
     * Metodo responsavel pelo login no servidor Home Assistant
     * @param view
     * @throws ExecutionException
     * @throws InterruptedException Excepção em caso de interrupção do processo de login ao servidor
     */
    public void login(View view) throws ExecutionException, InterruptedException {


        final EditText ip = findViewById(R.id.editText);
        final EditText password = findViewById(R.id.editText2);
        final EditText port = findViewById(R.id.editText3);
        final CheckBox caixaHttps = findViewById(R.id.checkBox);



        //Leitura dos dados anteriores de login
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mPreferences = getSharedPreferences("DadosLogin", Context.MODE_PRIVATE);
        mEditor=mPreferences.edit();




        TextView debugOutput = findViewById(R.id.textView5);
        String url;


        //Verificação se é Http ou Https
        if(caixaHttps.isChecked()){
            url="https://"+ip.getText()+":"+port.getText();
        }else{
            url="http://"+ip.getText()+":"+port.getText();
        }

        //Verificação através de conecção ao servidor



        String output;

        output= new JSONtaskGET().execute(url +"/api/", "" + password.getText()).get();
        debugOutput.setText(output);
        if(output.contains("API running")){
            Log.d("Output", "Funciona");
            Intent intent = new Intent(this, DashboardTV.class);

            //envio das variaveis de login para o nosso intent
            intent.putExtra("url", url);
            intent.putExtra("password", password.getText().toString());
            //Guardar as nossas variaveis para a proxima vez que iniciarmos a nossa aplicação
            mEditor.putString("ip",ip.getText().toString());
            mEditor.putString("password",password.getText().toString());
            mEditor.putString("port",port.getText().toString());
            mEditor.putBoolean("https",caixaHttps.isChecked());
            mEditor.commit();






            startActivity(intent);
        }else{
            Log.d("Output", "Dados invalidos!!!!");
            Toast toast = Toast.makeText(getApplicationContext(), "Dados invalidos!!!!", Toast.LENGTH_LONG);
            toast.show();


        }





    }

    /**
     * Metodo de teste responsavel por verificar a existencia do servidor Home Assistant
     * @param view
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void test(View view) throws InterruptedException, ExecutionException {
        final EditText ip = findViewById(R.id.editText);
        final EditText password = findViewById(R.id.editText2);
        final EditText port = findViewById(R.id.editText3);
        final CheckBox caixaHttps = findViewById(R.id.checkBox);
        TextView debugOutput = findViewById(R.id.textView5);
        String url;


        //Verificação se é Http ou Https
        if(caixaHttps.isChecked()){
            url="https://"+ip.getText()+":"+port.getText();
        }else{
            url="http://"+ip.getText()+":"+port.getText();
        }

        //Verificação através de conecção ao servidor



        String output;

        output= new JSONtaskGET().execute(url +"/api/", "" + password.getText()).get();
        debugOutput.setText(output);
        if(output.contains("API running")){
            Log.d("Output", "Funciona");
        }else{
                Log.d("Output", "Dados invalidos!!!!");
                Toast toast = Toast.makeText(getApplicationContext(), "Dados invalidos!!!!", Toast.LENGTH_LONG);
                toast.show();


        }


    }


}
