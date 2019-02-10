package com.example.a8_queenspuzzlegame;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
// bugs when 3, 4:
//    0, 0; 1, 0; 4, 0; 5, 0; 6, 0;

    int queens = 0;
    boolean invalidMove = false;
    int invalidID = -10;
    int qids[] = new int[]{-10, -10, -10, -10, -10, -10, -10, -10};
    int qx[] = new int[]{-10, -10, -10, -10, -10, -10, -10, -10};
    int qy[] = new int[]{-10, -10, -10, -10, -10, -10, -10, -10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void queenHandler(View v) {
        ImageButton currentButton = findViewById(v.getId());


        if (currentButton.getDrawable() == null && queens != 8) {
            if (!invalidMove && queens > 0) {
                Log.v("coordinates", "------------------- new button -------------------");
                currentButton.setImageDrawable(getResources().getDrawable(R.drawable.shrekface));
                placeQID(v);
                Log.v("coordinates", "placed");
                if (moveCheck(v)) {
                    invalidMove = true;
                    invalidID = v.getId();
                    Log.v("coordinates", "invalid move applying filter");
                    ColorFilter cf = new PorterDuffColorFilter(Color.rgb(255, 0, 0), PorterDuff.Mode.OVERLAY);
                    currentButton.setColorFilter(cf);
                }
            }

            if (queens == 0) {
                currentButton.setImageDrawable(getResources().getDrawable(R.drawable.shrekface));
                placeQID(v);
            }
            if (queens == 8 && !invalidMove) {
                Toast.makeText(v.getContext(), "You win!", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.v("coordinates", "------------------- button removed -------------------");
            currentButton.setImageDrawable(null);
            currentButton.setColorFilter(null);
            removeQID(v);
        }

        Log.v("coordinates", "invalidMove is " + invalidMove);
        Log.v("coordinates", "queens is " + queens);

    }

    void restart(View v) {
        Log.v("coordinates", "------------------- restarting -------------------");
        invalidMove = false;
        for (int i = 0; i < qids.length; i++) {
            if (qids[i] != -10) {
                ImageButton currentButton = findViewById(qids[i]);
                currentButton.setImageDrawable(null);
                removeQID(currentButton);
            }
        }
    }

    void placeQID(View v) {
        for (int i = 0; i < qids.length; i++) {
            if (qids[i] == -10) {
                Log.v("coordinates", "button's id: " + v.getId());
                qids[i] = v.getId();
                placeQueenRowCol(v, i);
                queens++;
                TextView text = findViewById(R.id.queensPlaced);
                text.setText("Queens Placed: " + queens + "/8");
                return;
            }
        }
    }

    void removeQID(View v) {
        if (invalidMove && v.getId() == invalidID) {
            invalidMove = false;
            invalidID = -10;
        }
        for (int i = 0; i < qids.length; i++) {
            if (qids[i] == v.getId()) {
                Log.v("coordinates", "removing " + qids[i] + " from qids");
                qids[i] = -10;
                removeQueenRowCol(i);
                queens--;
                TextView text = findViewById(R.id.queensPlaced);
                text.setText("Queens Placed: " + queens + "/8");
                return;
            }
        }
    }

    void placeQueenRowCol(View v, int i) {
        Log.v("coordinates", "button's i: " + i);
        Log.v("coordinates", "button's x: " +  (v.getX() - 11) / 131);
        Log.v("coordinates", "button's y: " +  (v.getY() - 11) / 131);
        Log.v("coordinates", "button's x: " + (int) (v.getX() - 11) / 131);
        Log.v("coordinates", "button's y: " + (int) (v.getY() - 11) / 131);
        qx[i] = (int) (v.getX() - 11) / 131;
        qy[i] = (int) (v.getY() - 11) / 131;

    }

    void removeQueenRowCol(int i) {
        Log.v("coordinates", "button's i: " + i);
        Log.v("coordinates", "removing " + qx[i] + " from qx");
        Log.v("coordinates", "removing " + qy[i] + " from qy");
        qx[i] = -10;
        qy[i] = -10;
    }

    int findQueeni(View v) {
        for (int i = 0; i < qids.length; i++) {
            if (qids[i] == v.getId()) {
                return i;
            }
        }
        return -10;
    }

    //    returns true if move is invalid
    boolean moveCheck(View v) {
        Log.v("coordinates", "checking move");
        int i = findQueeni(v);
        float currentX = qx[i];
        float currentY = qy[i];
        for (int j = 0; j < qx.length; j++) {
            if (currentX == qx[j] && j != i) {
                Log.v("coordinates", "beef on y");
                return true;
            }
            if (currentY == qy[j] && j != i) {
                Log.v("coordinates", "beef on x");
                return true;
            }
            if (checkDiag(i, j)) {
                return true;
            }

        }
        return false;
    }

    //    returns true if the button at i is diagonal from button j
    boolean checkDiag(int i, int j) {
        float ix = qx[i];
        float iy = qy[i];
        float jx = qx[j];
        float jy = qy[j];
        if(jx == -10 || jy == -10) {
            return false;
        }
        if(ix == jx && iy == jy) {
            return false;
        }

        return (upRight(ix, iy, jx, jy) || upLeft(ix, iy, jx, jy) || downLeft(ix, iy, jx, jy) || downRight(ix, iy, jx, jy));
    }
// the following functions return true if the buttons are in that diagonal
    boolean upRight(float ix, float iy, float jx, float jy) {
        Log.v("coordinates", "original i = (" + ix + ", " + iy + ")");
        Log.v("coordinates", "original j = (" + jx + ", " + jy + ")");
        while(jx >= 0 && jy <= 8) {
            if(ix == jx && iy == jy) {
                Log.v("coordinates", "beef on ur");
                Log.v("coordinates", "i = (" + ix + ", " + iy + ")");
                Log.v("coordinates", "j = (" + jx + ", " + jy + ")");
                return true;
            }
            jx--;
            jy++;
        }
        return false;
    }
    boolean upLeft(float ix, float iy, float jx, float jy) {
        while(jx <= 8 && jy <= 8) {
            if(ix == jx && iy == jy) {
                Log.v("coordinates", "beef on ul");
                Log.v("coordinates", "i = (" + ix + ", " + iy + ")");
                Log.v("coordinates", "j = (" + jx + ", " + jy + ")");
                return true;
            }
            jx++;
            jy++;
        }
        return false;
    }

    boolean downRight(float ix, float iy, float jx, float jy) {
        while(jx >= 0 && jy >= 0) {
            if(ix == jx && iy == jy) {
                Log.v("coordinates", "beef on dr");
                return true;
            }
            jx--;
            jy--;
        }
        return false;
    }

    boolean downLeft(float ix, float iy, float jx, float jy) {
        while(jx <= 8 && jy >= 0) {
            if(ix == jx && iy == jy) {
                Log.v("coordinates", "beef on dl");
                return true;
            }
            jx++;
            jy--;
        }
        return false;
    }
}
