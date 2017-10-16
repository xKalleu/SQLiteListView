package xkalleu.trab_hebert_sqllite;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btinserir,btlistar;
    ListView listaitems;
    ProgressDialog mprogressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btinserir = (Button) findViewById(R.id.btinserir);
        btlistar = (Button) findViewById(R.id.btlistar);
        listaitems = (ListView) findViewById(R.id.listaitems);

        btinserir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SqlDbHelper mDbHelper = new SqlDbHelper(getBaseContext());
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                //Seleciona o maior indice da tabela
                Cursor c = db.rawQuery("select max(cod_cliente) from clientes",null);
                int idcliente=0;
                //Verifica se ha items na tabela e se houver pega o indice dele
                if (c.moveToFirst())
                {
                    idcliente=c.getInt(0);
                }
                idcliente+=1;
                ContentValues values = new ContentValues();
                values.put("COD_CLIENTE", idcliente);
                values.put("NOME", "Kalleu - "+idcliente);
                values.put("CPFCGC", "0000000000/"+idcliente);
                long newRowId;
                newRowId = db.insert("CLIENTES",null,values);
            }
        });
        btlistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogressDialog = ProgressDialog.show(MainActivity.this, "Aguarde", "Verificando Produto(s)...");
                new Thread(new Runnable() {
                    Handler handler = new Handler();
                    List<Pessoa> listadepessoas = new ArrayList<Pessoa>();
                    String erro="";
                    public void run() {
                        try {
                            handler.post(new Runnable() {
                                public void run() {
                                    SqlDbHelper mDbHelper = new SqlDbHelper(getBaseContext());
                                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                                    Cursor c = db.query("CLIENTES",new String[]{"COD_CLIENTE","NOME","CPFCGC"},null,null,null,null,"NOME");
                                    boolean proximo = true;

                                    if (c.moveToFirst())
                                    {
                                        while (proximo)
                                        {
                                            Pessoa pessoa = new Pessoa();
                                            pessoa.setCodigo(c.getInt(0));
                                            pessoa.setNome(c.getString(1));
                                            pessoa.setCpf(c.getString(2));
                                            listadepessoas.add(pessoa);
                                            proximo=c.moveToNext();
                                        }
                                    }
                                    if (listadepessoas.size() > 0)
                                    {
                                        ArrayAdapter<Pessoa> adapter = new ArrayAdapter<Pessoa>(
                                                MainActivity.this,
                                                android.R.layout.simple_list_item_1, listadepessoas);
                                        listaitems.setAdapter(adapter);
                                    }
                                }
                            });
                        } catch (Exception e) {
                            mprogressDialog.dismiss();
                            erro = e.toString();
                            handler.post(new Runnable() {
                                public void run() {
                                    Log.i("Clientes","ERRO "+erro.toString());
                                }
                            });
                            Log.i("Clientes", "ERRO "+e.toString());
                        }
                        mprogressDialog.dismiss();
                    }
                }).start();

            }
        });
    }

}


