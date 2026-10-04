package com.example.demo.response;


public class ResponseAPI<T> {
	
	private String status;
    private T data;

    // Costruttore vuoto (utile per serializzazione/deserializzazione JSON)
    public ResponseAPI() {
    }

    // Costruttore con parametri
    public ResponseAPI(String status, T data) {
        this.status = status;
        this.data = data;
    }

    // Metodo helper statico per risposte di successo
    // Nota: new ResponseAPI<T>(...) esplicito evita il problema di type inference
    public static <T> ResponseAPI<T> success(T data) {
        return new ResponseAPI<T>("SUCCESS", data);
    }

    // Metodo helper statico per risposte di errore
    public static <T> ResponseAPI<T> error(String status, T data) {
        return new ResponseAPI<T>(status, data);
    }

    // Getter e Setter
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    
    

}
