package com.jonesclass.sung;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import java.util.ArrayList;

public class BodyFactory {
    private static BodyFactory thisInstance;
    private static World world;
    private static ArrayList<Body> bodies = new ArrayList<>();


    public static final int PLANET = 0;
    public static final int SATELLITE = 1;
    public static final int ASTEROID = 2;

    private BodyFactory(World world){
        this.world = world;
    }

    public static BodyFactory getInstance(World world){
        if(thisInstance == null){
            thisInstance = new BodyFactory(world);
        }
        return thisInstance;
    }

    static public FixtureDef makeFixture(int material, Shape shape){
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        switch(material){
            case 0:
                fixtureDef.density = 3f;
                fixtureDef.friction = 0.3f;
                fixtureDef.restitution = 0.1f;
                break;
            case 1:
                fixtureDef.density = 0.5f;
                fixtureDef.friction = 0.7f;
                fixtureDef.restitution = 0.3f;
                break;
            default:
                fixtureDef.density = 0.5f;
                fixtureDef.friction = 0.5f;
                fixtureDef.restitution = 0.3f;
        }
        return fixtureDef;
    }

    public static Body makeCirclePolyBody(float posx, float posy, float radius, int material, BodyType bodyType, boolean fixedRotation) {
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        boxBodyDef.fixedRotation = fixedRotation;

        Body boxBody = world.createBody(boxBodyDef);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius /2);
        boxBody.createFixture(makeFixture(material,circleShape));
        circleShape.dispose();
        if (material == PLANET) {
            boxBody.setUserData(new ObjectStats("Planet"));
        } else if (material == SATELLITE) {
            boxBody.setUserData(new ObjectStats("Satellite"));
        }
        bodies.add(boxBody);
        return boxBody;
    }

    public static Body makePolygonShapeBody(Vector2[] vertices, float x, float y, int material, BodyType bodyType) {
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = x;
        boxBodyDef.position.y = y;
        Body boxBody = world.createBody(boxBodyDef);
        boxBody.setUserData(new ObjectStats("Asteroid"));

        PolygonShape polygon = new PolygonShape();
        polygon.set(vertices);
        boxBody.createFixture(makeFixture(material, polygon));
        polygon.dispose();
        bodies.add(boxBody);
        return boxBody;
    }

    public static Body createAsteroid(Asteroid asteroid) {
       return makePolygonShapeBody(asteroid.getPoints(), asteroid.getX(),asteroid.getY(), BodyFactory.ASTEROID, BodyType.DynamicBody);

    }

    public static ArrayList<Body> getGameBodies() { return bodies; }

}
