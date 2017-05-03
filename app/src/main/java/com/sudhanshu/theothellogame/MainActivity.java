package com.sudhanshu.theothellogame;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    int n=8;    //Size of the basic 8X8 Grid of the othello game
    LinearLayout mainLayout;    //The Linear Layout variable for the main layout
    LinearLayout[] rows;  //The Linear Layout array for all the rows
    LinearLayout footerLayout;  //The layout at the footer of the main layout
    MyTextView[][] gameCells;   //The cells to store the game characters
    MyTextView leftFooter, rightFooter, leftFooterText, rightFooterText;   //Cells to store the cells captured as the game progresses
    String black="BLACK: ", white="WHITE: ";
    final static int BLACK = 1;
    final static int WHITE = 2;
    boolean playerBturn = true;
    int[] slopearrayx = {-1,0,1,1,1,0,-1,-1};
    int[] slopearrayy = {-1,-1,-1,0,1,1,1,0};
    int B = 0;
    int W = 0;
    int valid_moves_black = 0;
    int valid_moves_white = 0;
    boolean last_turn_null = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = (LinearLayout) findViewById(R.id.activity_main);
        setUpGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == R.id.newGame){
            resetBoard();
        }
        else if( id == R.id.mainScreen){
            Intent i =new Intent();
            i.setClass(this,StartUpScreen.class);
            startActivity(i);
        }
        return true;
    }

    private void resetBoard() {
        for( int i=0; i<n; i++){
            for( int j=0; j<n; j++){
                gameCells[i][j].setClicked(0);
                gameCells[i][j].setText("");
                gameCells[i][j].setClickable(false);
                gameCells[i][j].setValid(false);
            }
        }
        rightFooter.setBackgroundColor(ContextCompat.getColor(this,R.color.grey));
        leftFooter.setBackgroundColor(ContextCompat.getColor(this,R.color.grey));
        footerLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
        playerBturn = true;
        initialGameSetup();
        validMoves(playerBturn);
    }

    private void setUpGame() {
        gameCells = new MyTextView[n][n];
        rows = new LinearLayout[n];
        playerBturn = true;
        for( int i=0; i<n; i++){
            rows[i] = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            rows[i].setLayoutParams(params);
            rows[i].setOrientation(LinearLayout.HORIZONTAL);
            rows[i].setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
            mainLayout.addView(rows[i]);
        }

        footerLayout = new LinearLayout(this);
        LinearLayout.LayoutParams footparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2);
        footerLayout.setLayoutParams(footparams);
        footerLayout.setOrientation(LinearLayout.HORIZONTAL);
        mainLayout.addView(footerLayout);

        for( int i=0; i<n; i++){
            for ( int j=0; j<n; j++){
                gameCells[i][j] = new MyTextView(this);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                param.setMargins(2, 2, 2, 2);
                gameCells[i][j].setLayoutParams(param);
                gameCells[i][j].setTextSize(40);
                gameCells[i][j].setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
                gameCells[i][j].setPadding(2,2,2,2);
                gameCells[i][j].setClickable(false);
                gameCells[i][j].setId(j+10*i);
                gameCells[i][j].setClicked(0);
                gameCells[i][j].setCoor_x(i);
                gameCells[i][j].setCoor_y(j);
                gameCells[i][j].setOnClickListener(this);
                rows[i].addView(gameCells[i][j]);
            }
        }

        leftFooter = new MyTextView(this);
        rightFooter = new MyTextView(this);
        LinearLayout.LayoutParams foottextparams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        leftFooter.setTextSize(30);
        rightFooter.setTextSize(30);
        leftFooter.setText(black);
        rightFooter.setText(white);
        leftFooter.setBackgroundColor(ContextCompat.getColor(this, R.color.grey));
        rightFooter.setBackgroundColor(ContextCompat.getColor(this, R.color.grey));
        leftFooter.setLayoutParams(foottextparams);
        rightFooter.setLayoutParams(foottextparams);
        footerLayout.addView(leftFooter);
        footerLayout.addView(rightFooter);

        initialGameSetup();
        validMoves(playerBturn);
    }

    private void initialGameSetup() {
        gameCells[(n/2) - 1][(n/2) - 1].setText("W");
        gameCells[(n/2) - 1][(n/2) - 1].setClicked(WHITE);
        gameCells[(n/2) - 1][(n/2) - 1].setValid(false);

        gameCells[(n/2)][(n/2)].setText("W");
        gameCells[(n/2)][(n/2)].setClicked(WHITE);
        gameCells[(n/2)][(n/2)].setValid(false);

        gameCells[(n/2) - 1][(n/2)].setText("B");
        gameCells[(n/2)-1][(n/2)].setClicked(BLACK);
        gameCells[(n/2) - 1][(n/2)].setValid(false);

        gameCells[(n/2)][(n/2) - 1].setText("B");
        gameCells[(n/2)][(n/2) - 1].setClicked(BLACK);
        gameCells[(n/2)][(n/2) - 1].setValid(false);

    }

    private void validMoves(boolean Bturn) {
       // int dx,dy; // Variable to iterate
        boolean validity;
        refreshBoard();
        valid_moves_black = 0;
        valid_moves_white = 0;
        for( int i=0; i<n; i++){
            for( int j=0; j<n; j++){
                if(Bturn){
                    validity = checkMove(i,j,BLACK);
                    if(validity) {
                        gameCells[i][j].setClickable(true);
                        gameCells[i][j].setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
                        valid_moves_black += 1;
                    }
                }
                else{
                    validity = checkMove(i,j,WHITE);
                    if(validity) {
                        gameCells[i][j].setClickable(true);
                        gameCells[i][j].setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                        valid_moves_white += 1;
                    }
                }
            }
        }

        boolean status = checkGameOver();
        if(status){
            gameDisable();
        }
        if(playerBturn && valid_moves_black == 0) {
            if (last_turn_null) {
                endGame();
            } else {
                last_turn_null = true;
                playerBturn = !playerBturn;
                validMoves(playerBturn);
            }
        }
        else if( !(playerBturn) && valid_moves_white==0) {
             if (last_turn_null) {
                endGame();
            } else {
                 last_turn_null = true;
                 playerBturn = !playerBturn;
                 validMoves(playerBturn);
            }
        }
       else {
            last_turn_null = false;
        }
    }

    private void endGame() {
        gameDisable();
    }

    private void refreshBoard() {
        W=0;
        B=0;
        for( int i = 0; i<n; i++ ){
            for( int j=0; j<n ; j++){
                if(gameCells[i][j].getClicked()==0){
                    gameCells[i][j].setValid(false);
                    gameCells[i][j].setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
                    gameCells[i][j].setClickable(false);
                }
                if(gameCells[i][j].getClicked() == 2){
                    gameCells[i][j].setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
                    W=W+1;
                }
                if(gameCells[i][j].getClicked() == 1){
                    gameCells[i][j].setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
                    B=B+1;
                }
            }
        }

        String lf,rf;
        lf = black + B;
        rf = white + W;
        leftFooter.setText(lf);
        rightFooter.setText(rf);

    }

    private void gameDisable() {
        int b_count = 0, w_count = 0;
        for( int i=0; i < n; i++){
            for( int j=0; j < n; j++){
                gameCells[i][j].setClickable(false);
                if(gameCells[i][j].getClicked() == BLACK){
                    b_count += 1;
                }
                else if(gameCells[i][j].getClicked() == WHITE){
                    w_count += 1;
                }
            }
        }
        if( b_count > w_count ){
            Toast.makeText(this,"Player BLACK WINS !!!",Toast.LENGTH_SHORT).show();
            leftFooter.setBackgroundColor(ContextCompat.getColor(this,R.color.green));
            rightFooter.setBackgroundColor(ContextCompat.getColor(this,R.color.red));
        }
        else if( b_count < w_count){
            Toast.makeText(this,"Player WHITE WINS !!!",Toast.LENGTH_SHORT).show();
            rightFooter.setBackgroundColor(ContextCompat.getColor(this,R.color.green));
            leftFooter.setBackgroundColor(ContextCompat.getColor(this,R.color.red));
        }
        else {
            Toast.makeText(this,"GAME DRAW !!!",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkGameOver() {
        int b=0;
        int w=0;
        for( int i=0; i<n; i++) {
            for(int j=0; j<n; j++){
               if(gameCells[i][j].getClicked()==BLACK){
                 b += 1;
               }
               else if(gameCells[i][j].getClicked()==WHITE){
                   w += 1;
               }
            }
        }
        int total = b+w;
        if(total == 64) {
            if( b > w){
                Toast.makeText(this,"Player BLACK WINS !!!",Toast.LENGTH_SHORT).show();
                leftFooter.setBackgroundColor(ContextCompat.getColor(this,R.color.green));
            }
            else if(b < w){
                Toast.makeText(this,"Player WHITE WINS !!!",Toast.LENGTH_SHORT).show();
                rightFooter.setBackgroundColor(ContextCompat.getColor(this,R.color.red));
            }
            else {
                Toast.makeText(this,"GAME DRAW !!!",Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        else {
            return false;
        }
    }

    private boolean checkMove(int i, int j, int player) {
        int flag=0;
        int move_x,move_y;
        if(gameCells[i][j].getClicked()==0) {
            if (player == BLACK) {
                for (int p = 0; p < 8; p++) {
                    move_x = i + slopearrayx[p];
                    move_y = j + slopearrayy[p];
                    flag = 0;
                    while (move_x < n && move_x >= 0 && move_y >= 0 && move_y < n) {
                        if (gameCells[move_x][move_y].getClicked() == WHITE) {
                            move_x = move_x + slopearrayx[p];
                            move_y = move_y + slopearrayy[p];
                        } else if (gameCells[move_x][move_y].getClicked() == BLACK) {
                            if(gameCells[move_x-slopearrayx[p]][move_y-slopearrayy[p]].getClicked()==WHITE) {
                                gameCells[move_x][move_y].setValid(true);
                                flag = 1;
                            }
                            break;
                        } else if(gameCells[move_x][move_y].getClicked() == 0) {
                            break;
                        }
                    }
                    if (flag == 1) {
                        return true;
                    }
                }
            } else if (player == WHITE) {
                for (int p = 0; p < 8; p++) {
                    move_x = i + slopearrayx[p];
                    move_y = j + slopearrayy[p];
                    flag = 0;
                    while (move_x < n && move_x >= 0 && move_y >= 0 && move_y < n) {
                        if (gameCells[move_x][move_y].getClicked() == BLACK) {
                            move_x = move_x + slopearrayx[p];
                            move_y = move_y + slopearrayy[p];
                        } else if (gameCells[move_x][move_y].getClicked() == WHITE) {
                            if(gameCells[move_x-slopearrayx[p]][move_y-slopearrayy[p]].getClicked()==BLACK) {
                                gameCells[move_x][move_y].setValid(true);
                                flag = 1;
                            }
                            break;
                        } else if(gameCells[move_x][move_y].getClicked() == 0) {
                            break;
                        }
                    }
                    if (flag == 1) {
                        return true;
                    }
                }
            }
        }
            return false;
    }

    public void onClick(View v) {
        MyTextView t =(MyTextView) v;
        int x,y;
        x = t.getCoor_x();
        y = t.getCoor_y();
        if(t.getClicked() == 0) {
            if(playerBturn){
                t.setClicked(BLACK);
                t.setText("B");
                changeCells(x,y,BLACK);
            } else{
                t.setClicked(WHITE);
                t.setText("W");
                changeCells(x,y,WHITE);
            }
            playerBturn = !playerBturn;
            boolean game_over;
            game_over = checkGameOver();
            if(game_over){
                gameDisable();
            }
            validMoves(playerBturn);
        }
    }

    private void changeCells(int x, int y, int P_id) {
        int move_x, move_y;
        int i,j;
        boolean coversion;
        for( int k =0 ;k < 8; k++) {
            move_x = x + slopearrayx[k];
            move_y = y + slopearrayy[k];
            i = move_x;
            j = move_y;
            coversion = false;
            if (i >= 0 && i < n && j >= 0 && j < n) {
                if(P_id == BLACK) {
                    while (i >= 0 && i < n && j >= 0 && j < n){
                        if (gameCells[i][j].getClicked() == WHITE){
                            i = i + slopearrayx[k];
                            j = j + slopearrayy[k];
                        }
                        else if (gameCells[i][j].getClicked() == BLACK){
                            coversion = true;
                            break;
                        }
                        else {
                            break;
                        }
                    }
                    if(coversion){
                        //gameCells[move_x][move_y].setText("B");
                        //gameCells[move_x][move_y].setClicked(BLACK);
                        while (gameCells[move_x][move_y].getClicked()==WHITE) {//&& move_x != (i + slopearrayx[k])) {
                            /* if(move_x == i){
                                break;
                            }*/
                            gameCells[move_x][move_y].setText("B");
                            gameCells[move_x][move_y].setClicked(BLACK);
                            move_x += slopearrayx[k];
                            move_y += slopearrayy[k];
                        }
                    }
                }
                else if(P_id == WHITE){
                    while (i >= 0 && i < n && j >= 0 && j < n){
                        if (gameCells[i][j].getClicked() == BLACK){
                            i = i + slopearrayx[k];
                            j = j + slopearrayy[k];
                        }
                        else if (gameCells[i][j].getClicked() == WHITE){
                            coversion = true;
                            break;
                        }
                        else {
                            break;
                        }
                    }
                    if(coversion) {
                        //gameCells[move_x][move_y].setText("W");
                        //gameCells[move_x][move_y].setClicked(WHITE);
                        while (gameCells[move_x][move_y].getClicked()==BLACK) {// && move_x != (i + slopearrayx[k])) {
                           /* if(move_x == i){
                                break;
                            } */
                            gameCells[move_x][move_y].setText("W");
                            gameCells[move_x][move_y].setClicked(WHITE);
                            move_x += slopearrayx[k];
                            move_y += slopearrayy[k];
                        }
                    }
                }
            }
        }
    }

}
