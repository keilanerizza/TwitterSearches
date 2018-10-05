package keilane.com.twittersearches;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends ListActivity {

    private static final String SEARCHES = "searches";

    private EditText queryEditText; // EditText onde o usuário digita uma consulta
    private EditText tagEditText; // EditText onde o usuário identifica uma consulta
    private SharedPreferences savedSearches; // pesquisas favoritas do usuário
    private ArrayList<String> tags; // lista de identificadores das pesquisar salvas
    private ArrayAdapter<String> adapter; // vincula identificadores a ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // obtém referências para os EditText
        queryEditText = findViewById(R.id.queryEditText);
        tagEditText = findViewById(R.id.tagEditText);

        // obtém os SharedPreferences que contêm as pesquisas salvas do usuário
        savedSearches = getSharedPreferences(SEARCHES, MODE_PRIVATE);

        //armazena os identificadores salvos em um ArrayList e, então, os ordena
        tags = new ArrayList<String>(savedSearches.getAll().keySet());
        Collections.sort(tags, String.CASE_INSENSITIVE_ORDER);

        // cria ArrayAdapter e o utiliza para vincular os identificadores a ListView
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, tags);
        setListAdapter(adapter);

        // registra receptor para salvar uma pesquisa nova ou editada
        ImageButton saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveButtonListener);

        // registra receptor que pesquisa quando o usuário toca em um identificador
        getListView().setOnItemClickListener(itemLongClickListener);

        // configura o receptor que permite ao usuário excluir ou editar uma pesquisa
        getListView().setOnItemClickListener(itemLongClickListener);
    } // fim do método onCreate

    // saveButtonListener salva um par identificador-consulta em SharedPreferences
    public OnClickListener saveButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // cria identificador se nem queryEditText nem tagEditTag está vazio
            if (queryEditText.getText().length() > 0 && tagEditText.getText().length() > 0) {
                addTaggedSearches(queryEditText.getText().toString(), tagEditText.getText().toString());
                queryEditText.setText(""); // limpa queryEditText
                tagEditText.setText(""); // limpa tagEditText

                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(tagEditText.getWindowToken(), 0);
            } else { // exibe mensagem solicitando que forneça uma consulta e um identificador
                // cria um novo AlertDialog Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                // configura o título da caixa de diálogo e a mensagem a ser exibida
                builder.setMessage(R.string.missingMessage);

                // fornece um botão OK que simplesmente remove a caixa de diálogo
                builder.setPositiveButton(R.string.OK, null);

                // cria AlertDialog a partir de AlertDialog.Builder
                AlertDialog errorDialog = builder.create();
                errorDialog.show(); // exibe a caixa de diálogo modal
            }
        } // fim do método onClick
    }; // fim da classe interna anônima onClickListener

    // adiciona uma nova pesquisa ao arquivo de salvamento e, então, atualiza todos os componentes Button
    private void addTaggedSearches(String query, String tag) {
        // obtém um objeto SharedPreferences.Editor para armazenar novos pares identificador-consulta
        SharedPreferences.Editor preferencesEditor = savedSearches.edit();
        preferencesEditor.putString(tag, query); // armazena pesquisa atual
        preferencesEditor.apply(); // armazena as preferencias atualizadas

        // se o identificador é novo, adiciona-o, ordena tags e exibe a lista atualizada
        if(!tags.contains(tag)) {
            tags.add(tag); // adiciona o novo identificador
            Collections.sort(tags, String.CASE_INSENSITIVE_ORDER);
            adapter.notifyDataSetChanged(); // vincula os identificadores a ListView
        }
    }

    // itemClickListener ativa o navegador Web para exibir resultados da busca
    OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // obtem a string de consulta e cria uma URL representando a busca

        }
    }
}
