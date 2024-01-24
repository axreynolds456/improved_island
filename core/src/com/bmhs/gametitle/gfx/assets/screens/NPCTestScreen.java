package com.bmhs.gametitle.gfx.assets.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import com.bmhs.gametitle.game.assets.characters.NonPlayerCharacter;
import com.bmhs.gametitle.game.utils.GameHandler;
import com.bmhs.gametitle.gfx.assets.tiles.Tile;
import com.bmhs.gametitle.gfx.assets.tiles.statictiles.WorldTile;
import com.bmhs.gametitle.gfx.utils.TileHandler;

public class NPCTestScreen implements Screen {


    GameHandler game;
    Screen parent;
    int numOfNPCs;

    Array<NonPlayerCharacter> npcArray;

    public NPCTestScreen(GameHandler game, Screen parent) {
        this.game = game;
        this.parent = parent;

        npcArray = new Array<>();

        numOfNPCs = 30;
        for(int i = 0; i < numOfNPCs; i++) {
            WorldTile tempTile = TileHandler.getTileHandler().getWorldTileArray().get(2);
            float tempX = (float) Math.random()* Gdx.graphics.getWidth();
            float tempY = (float) Math.random() * Gdx.graphics.getHeight();
            String tempName = "npc " + i;

            npcArray.add(new NonPlayerCharacter(tempTile, tempX, tempY, tempName));
        }
    }


   @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
//        for(int r = 0; r <Gdx.graphics.getHeight(); r+= Tile.ON_SCREEN_DEFAULT_HEIGHT) {
//            for(int c = 0; c < Gdx.graphics.getWidth(); c+= Tile.ON_SCREEN_DEFAULT_WIDTH) {
//                WorldTile tempTile = TileHandler.getTileHandler().getWorldTileArray().get(2);
//                game.batch.draw(tempTile.getTexture(), c, r);
//            }
//        }

        for(int i = 0; i < numOfNPCs; i++) {
            NonPlayerCharacter tempChar = npcArray.get(0);
            tempChar.tickTree();
            game.batch.draw(tempChar.getTile().getTexture(), tempChar.getX(), tempChar.getY());
        }

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
