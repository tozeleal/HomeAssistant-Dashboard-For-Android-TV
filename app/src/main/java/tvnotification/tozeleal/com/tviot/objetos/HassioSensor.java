package tvnotification.tozeleal.com.tviot.objetos;

/**
 * Class java responsavel pelo Objeto do sensor inteligente
 * Atualmente até a data existem 4 tipos conhecidos de sensores a usar esta classe:
 * 0 - Interruptores inteligentes
 * 1 - Lampadas Inteligentes
 * 2 - Scripts de Automação
 * 3 - Dispositivos Android (para a leitura do estado da bateria)
 * @Author António José Velez Leal (8150053)
 * Projeto Final 2017-2018
 * ESTG
 * LEI
 *
 */
public class HassioSensor {
    private String id;
    private String friendlyName;
    private String state;
    private String powerConsumed;
    private String inUse;
    private int type;


    /**
     * Metodo construtor do Sensor Inteligente - {@link HassioSensor}
     */
    public HassioSensor() {
        this.id = null;
        this.friendlyName = null;
        this.state = null;
        this.powerConsumed=null;
        this.type= -1;

        this.inUse=null;

    }


    /**
     * Metodo responsavel que retorna se a ficha inteligente está atualmente em uso
     * @return Uma String binaria do estado de uso da Ficha Inteligente
     */
    public String getInUse() {
        return inUse;
    }

    /**
     * Metodo responsavel que define o valor booleano da Ficha Inteligente está em uso
     * @param inUse Uma String binaria do estado de uso da Ficha Inteligente
     */
    public void setInUse(String inUse) {
        this.inUse = inUse;
    }


    /**
     * Metodo responsavel que retorna valor da energia consumida de uma ficha inteligente
     * @return O valor da energia consumida em Wh (watts hora)
     */
    public String getPowerConsumed() {
        return powerConsumed;
    }

    /**
     * Metodo responsavel que define o valor da energia consumida de uma ficha inteligente
     * @param powerConsumed O valor da energia consumida em Wh (watts hora)
     */
    public void setPowerConsumed(String powerConsumed) {
        this.powerConsumed = powerConsumed;
    }

    /**
     * Metodo responsavel que retorna o tipo de sensor inteligente
     * @return Codigo identificador do tipo de sensor inteligente
     */
    public int getType() {
        return type;
    }

    /**
     * Metodo responsavel que define o tipo de sensor inteligente
     * @param type Codigo identificador do tipo de sensor inteligente
     */
    public void setType(int type) {
        this.type = type;
    }


    /**
     * Metodo responsavel por retornar o codigo identificador do sensor inteligente dentro do sistema Home Assistant
     * @return Codigo identificador do sensor inteligente dentro do sistema Home Assistant
     */
    public String getId() {
        return id;
    }

    /**
     * Metodo responsavel por definir o codigo identificador do sensor inteligente dentro do sistema Home Assistant
     * @param id Codigo identificador do sensor inteligente dentro do sistema Home Assistant
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Metodo responsavel por retornar o Nome amigavel ao utilizador do sensor Inteligente (Pode alterar o nome amigavel do sensor inteligente no configuration.yaml)
     * @return O nome amigavel do sensor inteligente
     */
    public String getFriendlyName() {
        return friendlyName;
    }

    /**
     * Metodo responsavel por definir o Nome amigavel ao utilizador do sensor Inteligente
     * @param friendlyName O nome amigavel do sensor inteligente
     */
    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    /**
     * Metodo responsavel por retornar o estado atual do sensor
     * @return Estado do sensor inteligente
     */
    public String getState() {
        return state;
    }

    /**
     * Metodo responsavel por definir o estado atual do sensor
     * @param state Estado do sensor inteligente
     */
    public void setState(String state) {
        this.state = state;
    }


}