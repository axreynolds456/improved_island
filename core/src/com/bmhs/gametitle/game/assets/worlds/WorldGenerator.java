package com.bmhs.gametitle.game.assets.worlds;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bmhs.gametitle.gfx.assets.tiles.statictiles.WorldTile;
import com.bmhs.gametitle.gfx.utils.TileHandler;


public class WorldGenerator {

    private int worldMapRows, worldMapColumns;

    private int[][] worldIntMap;

    private int seedColor, lightGreen, green;




    public WorldGenerator (int worldMapRows, int worldMapColumns) {
        this.worldMapRows = worldMapRows;
        this.worldMapColumns = worldMapColumns;

        worldIntMap = new int[worldMapRows][worldMapColumns];

        //call methods to build 2D array

        seedColor = 2;
        lightGreen = 17;


        Vector2 mapSeed = new Vector2(MathUtils.random(worldIntMap[0].length), MathUtils.random(worldIntMap.length));
        System.out.println(mapSeed.y + "" + mapSeed.x);

        worldIntMap[(int)mapSeed.y][(int)mapSeed.x] = 4;

        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                Vector2 tempVector = new Vector2(c,r);
                if(tempVector.dst(mapSeed) < 10) {
                    worldIntMap[r][c] = 6;
                }
            }
        }


        //randomize();
        ocean();
        seedMap(2);
        setSeedMap(24,56,4);
        miniIslands(5,10, seedColor, lightGreen, 0.99);
        searchAndExpand(10, seedColor, 44, 0.99);
        searchAndExpand(8, seedColor, 22, 0.85);
        searchAndExpand(6, seedColor, 72, 0.55);
        searchAndExpand(5, seedColor, 30, 0.65);
        searchAndExpand(4, seedColor, 39, 0.25);


        generateWorldTextFile();

        Gdx.app.error("WorldGenerator", "WorldGenerator(WorldTile[][][])");
    }

    public String getWorld3DArrayToString() {
        String returnString = "";

        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                returnString += worldIntMap[r][c] + " ";
            }
            returnString += "\n";
        }

        return returnString;
    }

    public void ocean(){
        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                worldIntMap[r][c] = 19;
            }
        }
    }


    public void seedMap(int num) {
        for(int i = 0; i < num; i++) {
            int islandRow = (int) (Math.random() * worldMapRows);
            int islandCol = (int) (Math.random() * worldMapColumns);

            int islandSize = (int) (Math.random() * 50) + 50;

            setSeedMap(islandRow, islandCol, islandSize);
        }
    }

    public void setSeedMap(int row, int col, int size) {
        int halfSize = size / 2;
        worldIntMap[row][col] = 1;
        Vector2 mapSeed = new Vector2(row, col);

        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                Vector2 tempVector = new Vector2(c,r);
                if(tempVector.dst(mapSeed) < 10) {
                    worldIntMap[r][c] = 16;
                }
            }
        }

        int numPoints = size * 100;

        for(int i = 0; i < numPoints; i++) {
            double angle = Math.random() * 2 * Math.PI;
            double distance = Math.sqrt(Math.random()) * halfSize;


            int xOffset = (int) (Math.cos(angle) * distance);
            int yOffset = (int) (Math.sin(angle) * distance);

            int islandX = col + xOffset;
            int islandY = row + yOffset;

            if( islandX >= 0 && islandX < worldMapColumns && islandY >= 0 && islandY < worldMapRows) {
                worldIntMap[islandY][islandX] = 45;
            }
        }

    }

    private void miniIslands(int numIslands, int radius, int numToFind, int numToWrite, double prob) {
        for(int i = 0; i < numIslands; i++) {
            int rSeed = MathUtils.random(worldIntMap.length-1);
            int cSeed = MathUtils.random(worldIntMap[0].length-1);
            worldIntMap[rSeed][cSeed] = seedColor;
        }
    }

    private void searchAndExpand(int radius, int numToFind, int numToWrite, double probability) {
        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                if(worldIntMap[r][c] == numToFind) {
                    for(int subRow = r-radius; subRow <= r+radius; subRow++) {
                        for(int subCol = c-radius; subCol <= c+radius; subCol++) {
                            if(subRow >= 0 && subCol >= 0 && subRow <= worldIntMap.length-1 && subCol <= worldIntMap[0].length-1 && worldIntMap[subRow][subCol] != numToFind) {
                                if(Math.random() > probability) {
                                    worldIntMap[subRow][subCol] = numToWrite;
                                }
                            }

                        }
                    }

                }
            }
        }
    }

    public void randomize() {
        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                worldIntMap[r][c] = MathUtils.random(TileHandler.getTileHandler().getWorldTileArray().size-1);
            }
        }
    }

    public WorldTile[][] generateWorld() {
        WorldTile[][] worldTileMap = new WorldTile[worldMapRows][worldMapColumns];
        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                worldTileMap[r][c] = TileHandler.getTileHandler().getWorldTileArray().get(worldIntMap[r][c]);
            }
        }
        return worldTileMap;
    }





    private void generateWorldTextFile() {
        FileHandle file = Gdx.files.local("assets/worlds/world.text");
        file.writeString(getWorld3DArrayToString(), false);
    }

}
