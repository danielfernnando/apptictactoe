package devnoite.progandroid.jogodavelhaapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

import devnoite.progandroid.jogodavelhaapp.R;
import devnoite.progandroid.jogodavelhaapp.databinding.FragmentJogoBinding;
import devnoite.progandroid.jogodavelhaapp.util.PrefsUtil;


public class JogoFragment extends Fragment {
    //variável para acessa ros elementos da View
    private FragmentJogoBinding binding;
    //vetor de botões para referenciar os botões
    private Button[] botoes;
    //matriz de String que reperesenta o tabuleiro
    private String[][] tabuleiro;
    //variáveis para os símbolos
    private String simbJog1, simbJog2, nome1, nome2, simbolo;
    //variável Random para determinar quem inicia o jogo
    private Random ramdom;
    //variável para controlar o número de jogadas
    private int numJogadas = 0, velha = 0;
    //variáveis para o placar
    private int placarJog1 = 0, placarJog2 = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //habilitar o menu
        setHasOptionsMenu(true);
        //instanciar a binding
        binding = FragmentJogoBinding.inflate(inflater, container, false);

        //instanciar o vetor
        botoes = new Button[9];

        //associar o vetor aos botões
        botoes[0] = binding.bt00;
        botoes[1] = binding.bt01;
        botoes[2] = binding.bt02;
        botoes[3] = binding.bt10;
        botoes[4] = binding.bt11;
        botoes[5] = binding.bt12;
        botoes[6] = binding.bt20;
        botoes[7] = binding.bt21;
        botoes[8] = binding.bt22;

        //associa o listener aos botões
        for (Button bt : botoes) {
            bt.setOnClickListener(listenerBotoes);
        }
        //instanciar o tabuleiro
        tabuleiro = new String[3][3];

        //preenchendo a matriz com Strings vazias ("")
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                tabuleiro[i][j] = "";
            }
        }
        for (String[] vetor: tabuleiro
             ) {
            Arrays.fill(vetor, "");
        }

        //define os símbolos do jogador1 e do jogador 2
        simbJog1 = PrefsUtil.getSimboloJog1(getContext());
        simbJog2 = PrefsUtil.getSimboloJog2(getContext());

        //define os nomes dos jogadores
        nome1 = PrefsUtil.getNomeJog1(getContext());
        nome2 = PrefsUtil.getNomeJog2(getContext());

        //atualizar o placar com os símbolos
        binding.primeiroJogador.setText(getResources().getString(R.string.nomeJog1,nome1)+simbJog1);
        binding.segundoJogador.setText(getResources().getString(R.string.nomeJog2,nome2)+simbJog2);

        //instancia o Random
        ramdom = new Random();

        //sorteia quem iniciará o jogo
        sorteia();
        atualizarVez();


        //retorna a view root do binding
        return binding.getRoot();
    }
    @Override
    public void onStart(){
        super.onStart();
        //pega a referência para a activity
        AppCompatActivity minhaActivity = (AppCompatActivity) getActivity();
        //oculta a action bar
        minhaActivity.getSupportActionBar().show();
        //desabilita a seta de retornar
        minhaActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void sorteia() {
        //se gerar um valor verdadeiro Jog1 começa, caso contrário o Jog2 começa
        if (ramdom.nextBoolean()) {
            simbolo = simbJog1;
        } else {
            simbolo = simbJog2;
        }
    }

    //método que atualiza o placar
    private void atualizaPlacar(){
        binding.contJog1.setText(placarJog1+"");
        binding.contJog2.setText(placarJog2+"");
        binding.contVelha.setText(velha+"");
    }

    private void atualizarVez() {
        if (simbolo.equals(simbJog1)) {
            binding.primeiroJogador.setBackgroundColor(getResources().getColor(R.color.azul));
            binding.primeiroJogador.setTextColor(getResources().getColor(R.color.white));
            binding.segundoJogador.setBackgroundColor((getResources().getColor(R.color.white)));
        } else{
            binding.segundoJogador.setBackgroundColor(getResources().getColor(R.color.amarelo));
            binding.primeiroJogador.setTextColor(getResources().getColor(R.color.black));
            binding.primeiroJogador.setBackgroundColor(getResources().getColor(R.color.white));
        }

    }

    //verifica se venceu nas linhas
    private boolean venceu() {
        for (int li = 0; li < 3; li++) {
            if (tabuleiro[li][0].equals(simbolo) && tabuleiro[li][1].equals(simbolo) && tabuleiro[li][2].equals(simbolo)) {
                return true;
            }
        }
        //verifica se venceu nas colunas
        for (int col = 0; col < 3; col++) {
            if (tabuleiro[0][col].equals(simbolo) && tabuleiro[1][col].equals(simbolo) && tabuleiro[2][col].equals(simbolo)) {
                return true;
            }
        }
        //verifica se venceu nas diagonais
        if (tabuleiro[0][0].equals(simbolo) && tabuleiro[1][1].equals(simbolo) && tabuleiro[2][2].equals(simbolo)) {
            return true;
        }
        if (tabuleiro[0][2].equals(simbolo) && tabuleiro[1][1].equals(simbolo) && tabuleiro[2][0].equals(simbolo)) {
            return true;
        }
        return false;
    }

    private void resetar(){
        //percorrer os botões do vetor
        for (Button botao:
             botoes) {
            botao.setClickable(true);
            botao.setText("");
        }
        //limpar a matriz
        for (String[] vetor: tabuleiro
             ) {
            Arrays.fill(vetor,"");
        }
        numJogadas = 0;
        //sorteio o próximo
        sorteia();
        //atualiza a vez
        atualizarVez();
    }
    public Dialog onCreateDialog() {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Deseja realmente resetar?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        placarJog1 = 0;
                        placarJog2 = 0;
                        velha = 0;
                        atualizaPlacar();
                        resetar();
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //verifica qual item do menu foi selecionado
        switch (item.getItemId()){
            //caso seja a opção resetar
            case R.id.menu_resetar:
                onCreateDialog().show();
                break;

                //caso seja a opção de preferências
            case R.id.menu_prefs:
                NavHostFragment.findNavController(JogoFragment.this).
                    navigate(R.id.action_jogoFragment_to_prefFragment);
        }
        return true;

    }

    //listener para os botões
    private View.OnClickListener listenerBotoes = btPress -> {

        numJogadas++;
        //obtém o nome do botão
        String nomeBotao = getContext().getResources().getResourceName(btPress.getId());
        //extrai a posição através do nome do botão
        String posicao = nomeBotao.substring(nomeBotao.length() - 2);
        //extrai linha e coluna da string posição
        int linha = Character.getNumericValue(posicao.charAt(0));
        int coluna = Character.getNumericValue(posicao.charAt(1));
        //preencher a posição da matriz com o símbolo "da vez"
        tabuleiro[linha][coluna] = simbolo;
        //faz um casting de View pra Button
        Button botão = (Button) btPress;
        //"seta" o símbolo no botão pressionado
        botão.setText(simbolo);
        //trocar o background do botão quando clicado

        //desabilitar o botão que foi pressionado
        botão.setClickable(false);
        //verifica se venceu
        if (numJogadas >= 5 && venceu()){
            if (simbolo.equals(simbJog1)){
                placarJog1++;
                Toast.makeText(getContext(), R.string.jog1Venceu, Toast.LENGTH_LONG).show();
            }else {
                placarJog2++;
                Toast.makeText(getContext(), R.string.jog2Venceu, Toast.LENGTH_LONG).show();
            }
            //atualiza o placar
            atualizaPlacar();
            //reseta
            resetar();
            //informa que houve um vencedor
            //Toast.makeText(getContext(), R.string.venceu, Toast.LENGTH_LONG).show();
        }else if (numJogadas == 9){
            velha++;
            atualizaPlacar();
            //informa que deu velha
            Toast.makeText(getContext(), R.string.velha, Toast.LENGTH_LONG).show();
            //reseta
            resetar();

        }else {
            //inverte o símbolo
            simbolo = simbolo.equals(simbJog1) ? simbJog2 : simbJog1;
            atualizarVez();
        }



//        segunda forma de inverter
//        if(simbolo.equals(simbJog1)){
//            simbolo = simbJog2;
//        }else {
//            simbolo = simbJog1;
//        }





        Log.w("BOTAO", linha+"");
        Log.w("BOTAO", coluna+"");
    };
}