package Pacman;
 
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.*;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import java.awt.Font;
import java.io.InputStream;
import org.newdawn.slick.util.ResourceLoader;
 
public class Play extends BasicGameState {
       
        Player player = new Player(15*32,14*32,3,"Player1");
        Enemy ghost[] = {new Enemy(448,384),new Enemy(480,384),new Enemy(480,384),new Enemy(512,384),new Enemy(544,384)};
        Level collisionLevel = new Level();
        Level coinLevel = new Level();
        Image coin;
        Image powerUp;
        TrueTypeFont font2;
       
        long runningTime;
        boolean dieani;
       
       
        private static final int MAX_PATH_LENGTH = 100;
    Path path;
    AStarPathFinder pathFinder;
    SimpleMap map;
    int mover;
    Shape[] test = new Rectangle[MAX_PATH_LENGTH];
   
   
   
        /**
         *
         * @param state the integer: The state
         */
        public Play(int state){
        }
       
        /**
         *
         */
        @Override
        public void init(GameContainer gc, StateBasedGame sbg)throws SlickException{
                dieani = false;
                runningTime = 0;
                collisionLevel.SetMap("data/map/map.tmx");
                collisionLevel.SetHitBox("collider");
               
                coin = new Image("data/map/coin.png");
                coinLevel.SetMap("data/map/map.tmx","data/sounds/pacmanEat.ogg");
                coinLevel.SetHitBox("coinPath");
               
                player.SetAniUp("data/pacman/PacmanUp1.png","data/pacman/PacmanUp2.png","data/pacman/PacmanBallUpDown.png");
                player.SetAniDown("data/pacman/PacmanDown1.png", "data/pacman/PacmanDown2.png", "data/pacman/PacmanBallUpDown.png");
                player.SetAniLeft("data/pacman/PacmanLeft1.png","data/pacman/PacmanLeft2.png","data/pacman/PacmanBallLeftRight.png");
                player.SetAniRight("data/pacman/PacmanRight1.png", "data/pacman/PacmanRight2.png", "data/pacman/PacmanBallLeftRight.png");
                player.SetDieAni("data/pacman/die/PacmanDie1.png", "data/pacman/die/PacmanDie2.png", "data/pacman/die/PacmanDie3.png", "data/pacman/die/PacmanDie4.png", "data/pacman/die/PacmanDie5.png", "data/pacman/die/PacmanDie6.png", "data/pacman/die/PacmanDie7.png", "data/pacman/die/PacmanDie8.png", "data/pacman/die/PacmanDie9.png", "data/pacman/die/PacmanDie10.png", "data/pacman/die/PacmanDie11.png", "data/pacman/die/PacmanDie12.png", 200);
                player.SetSprite(player.GetAniRight());
                player.SetPath(collisionLevel);
               
                ghost[0].SetAniUp("data/ghost/red/GhostUp1.png","data/ghost/red/GhostUp2.png","data/ghost/red/GhostUp1.png");
                ghost[0].SetAniDown("data/ghost/red/GhostDown1.png","data/ghost/red/GhostDown2.png","data/ghost/red/GhostDown1.png");
                ghost[0].SetAniLeft("data/ghost/red/GhostLeft1.png","data/ghost/red/GhostLeft2.png","data/ghost/red/GhostLeft1.png");
                ghost[0].SetAniRight("data/ghost/red/GhostRight1.png","data/ghost/red/GhostRight2.png","data/ghost/red/GhostRight1.png");
                ghost[0].SetSprite(ghost[0].GetAniUp());
                ghost[0].SetPath(collisionLevel);
               
                ghost[1].SetAniUp("data/ghost/cyan/GhostUp1.png","data/ghost/cyan/GhostUp2.png","data/ghost/cyan/GhostUp1.png");
                ghost[1].SetAniDown("data/ghost/cyan/GhostDown1.png","data/ghost/cyan/GhostDown2.png","data/ghost/cyan/GhostDown1.png");
                ghost[1].SetAniLeft("data/ghost/cyan/GhostLeft1.png","data/ghost/cyan/GhostLeft2.png","data/ghost/cyan/GhostLeft1.png");
                ghost[1].SetAniRight("data/ghost/cyan/GhostRight1.png","data/ghost/cyan/GhostRight2.png","data/ghost/cyan/GhostRight1.png");
                ghost[1].SetSprite(ghost[1].GetAniUp());
                ghost[1].SetPath(collisionLevel);
               
                ghost[2].SetAniUp("data/ghost/orange/GhostUp1.png","data/ghost/orange/GhostUp2.png","data/ghost/orange/GhostUp1.png");
                ghost[2].SetAniDown("data/ghost/orange/GhostDown1.png","data/ghost/orange/GhostDown2.png","data/ghost/orange/GhostDown1.png");
                ghost[2].SetAniLeft("data/ghost/orange/GhostLeft1.png","data/ghost/orange/GhostLeft2.png","data/ghost/orange/GhostLeft1.png");
                ghost[2].SetAniRight("data/ghost/orange/GhostRight1.png","data/ghost/orange/GhostRight2.png","data/ghost/orange/GhostRight1.png");
                ghost[2].SetSprite(ghost[2].GetAniUp());
                ghost[2].SetPath(collisionLevel);
               
                ghost[3].SetAniUp("data/ghost/pink/GhostUp1.png","data/ghost/pink/GhostUp2.png","data/ghost/pink/GhostUp1.png");
                ghost[3].SetAniDown("data/ghost/pink/GhostDown1.png","data/ghost/pink/GhostDown2.png","data/ghost/pink/GhostDown1.png");
                ghost[3].SetAniLeft("data/ghost/pink/GhostLeft1.png","data/ghost/pink/GhostLeft2.png","data/ghost/pink/GhostLeft1.png");
                ghost[3].SetAniRight("data/ghost/pink/GhostRight1.png","data/ghost/pink/GhostRight2.png","data/ghost/pink/GhostRight1.png");
                ghost[3].SetSprite(ghost[3].GetAniUp());
                ghost[3].SetPath(collisionLevel);
                
                try {
                    InputStream inputStream = ResourceLoader.getResourceAsStream("data/fonts/heavy_data.ttf");
                     
                    Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
                    awtFont2 = awtFont2.deriveFont(36f); // set font size
                    font2 = new TrueTypeFont(awtFont2, false);
                         
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }  
       
        /**
         *
         */
        @Override
        public void render(GameContainer gc, StateBasedGame sbg, Graphics g)throws SlickException{
                collisionLevel.GetMap().render(0,0);
                for(int x = 0; x<coinLevel.GetWidth();x++){
                        for(int y = 0; y<coinLevel.GetHeight();y++){
                                if(coinLevel.GetBlocked()[x][y]){
                                        coin.draw(x*32,y*32);
                                }
                        }
                }
                ghost[0].GetSprite().draw(ghost[0].GetPosX(),ghost[0].GetPosY());
                ghost[1].GetSprite().draw(ghost[1].GetPosX(),ghost[1].GetPosY());
                ghost[2].GetSprite().draw(ghost[2].GetPosX(),ghost[2].GetPosY());
                ghost[3].GetSprite().draw(ghost[3].GetPosX(),ghost[3].GetPosY());
                player.GetSprite().draw(player.GetPosX(),player.GetPosY());
                g.drawString("SCORE: "+player.GetScore(),27*32,13*32);
                g.drawString("LIVES: "+player.getLives(), 3*32, 13*32);
                
                font2.drawString(820, 420, "123456789", Color.white);
               
                /*for(int i = 0; i < ghost1.getPathLenght(); i++) {
                        g.setColor(Color.red);
                        g.draw(ghost1.getPathHitbox()[i]);
        }
                for(int i = 0; i<ghost2.getPathLenght();i++){
                        g.setColor(Color.cyan);
                        g.draw(ghost2.getPathHitbox()[i]);
                }
                for(int i = 0; i<ghost3.getPathLenght();i++){
                        g.setColor(Color.orange);
                        g.draw(ghost3.getPathHitbox()[i]);
                }
                for(int i = 0; i<ghost4.getPathLenght();i++){
                        g.setColor(Color.pink);
                        g.draw(ghost4.getPathHitbox()[i]);
                }
                for(int i = 0; i<player.getPathLenght();i++){
                        g.setColor(Color.yellow);
                        g.draw(player.getPathHitbox()[i]);
                }*/
               
        }
       
       
        /**
         *
         */
        @Override
        public void update(GameContainer gc, StateBasedGame sbg, int delta)throws SlickException{
                ghost[0].movePath(collisionLevel);
                ghost[0].Move(delta, gc.getHeight(), gc.getWidth());
                ghost[0].GetSprite().update(delta);
               
                ghost[1].movePath(collisionLevel);
                ghost[1].Move(delta, gc.getHeight(), gc.getWidth());
                ghost[1].GetSprite().update(delta);
               
                ghost[2].movePath(collisionLevel);
                ghost[2].Move(delta, gc.getHeight(), gc.getWidth());
                ghost[2].GetSprite().update(delta);
               
                ghost[3].movePath(collisionLevel);
                ghost[3].Move(delta, gc.getHeight(), gc.getWidth());
                ghost[3].GetSprite().update(delta);
               
                player.movePath(collisionLevel);
                player.Move(delta,gc.getHeight(),gc.getWidth());
                player.GetSprite().update(delta);
               
                Input input = gc.getInput();
                if(input.isKeyPressed(Input.KEY_UP)){
                        player.GoStepUp(coinLevel.GetMap(),coinLevel.GetIndexLayer());
                }else if(input.isKeyPressed(Input.KEY_DOWN)){
                        player.GoStepDown(coinLevel.GetMap(),coinLevel.GetIndexLayer());
                }else if(input.isKeyPressed(Input.KEY_LEFT)){
                        player.GoStepLeft(coinLevel.GetMap(),coinLevel.GetIndexLayer());
                }else if(input.isKeyPressed(Input.KEY_RIGHT)){
                        player.GoStepRight(coinLevel.GetMap(),coinLevel.GetIndexLayer());
                }
               
                for(int x = 0; x<coinLevel.GetWidth();x++){
                        for(int y = 0; y<coinLevel.GetHeight();y++){
                                if(player.getHitBox().getCenterX()==coinLevel.GetHitbox()[x][y].getCenterX()&&player.getHitBox().getCenterY()==coinLevel.GetHitbox()[x][y].getCenterY()){
                                        if(coinLevel.GetBlocked()[x][y]){
                                                coinLevel.playSound();
                                                player.SetScore(10);
                                                player.SetCoinsEaten(1);
                                        }
                                        coinLevel.SetBlocked(x, y, false);
                                }
                        }
                }
               
                for(int i = 0; i<ghost.length;i++){
                        if(player.getHitBox().intersects(ghost[i].getHitBox())&&runningTime>2200){
                                player.die();
                                player.SetScore(-200);
                                player.SetSprite(player.getDieAni());
                                runningTime = 0;
                                dieani = true;
                               
                        }
                }
                runningTime +=delta;
                if(dieani&&runningTime>2200){
                        player.SetSprite(player.GetAniRight());
                        dieani = false;
                }
               
                if(player.DEAD()){
                        sbg.enterState(3);
                }
               
                if(player.WIN()){
                        sbg.enterState(2);
                }
        }
       
        /**
         *
         */
        @Override
        public int getID(){
                return 1;
        }
}