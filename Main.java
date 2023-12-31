import java.awt.*;
import java.util.Scanner;

public class Main {

    //Settings
    public static final int MAX_STEPS = 5000;
    public static final double DT = 0.05;
    public static final boolean GRAVITY = true;

    public static final boolean enableInitDialogue = false;

    public static final int DEFAULT_COLLISION_IMMUNITY = 3;

    public static void main(String[] args) {


        //Create and initialize Objects
        Box b1 = new Box();
        b1.name = "Box 1";
        b1.height = 30;
        b1.width = 30;
        b1.position = new double[]{600,100};
        b1.momentum = new double[]{-20,0};
        b1.friction = 0.5;

        Box b2 = new Box();
        b2.name = "Box 2";
        b2.height = 30;
        b2.width = 30;
        b2.position = new double[]{150,100};
        b2.momentum = new double[]{40,0};

        Box b3 = new Box();
        b3.name = "Box 3";
        b3.height = 30;
        b3.width = 30;
        b3.position = new double[]{250,200};

        Box b4 = new Box();
        b4.name = "Box 4";
        b4.height = 40;
        b4.width = 20;
        b4.position = new double[]{250,300};

        Circle c1 = new Circle();
        c1.name = "c1";
        c1.mass = 3;
        c1.radius = 30;
        c1.position = new double[]{500,170};
        c1.momentum = new double[]{0,0};

        Circle c2 = new Circle();
        c2.name = "c2";
        c2.mass = 3;
        c2.radius = 30;
        c2.position = new double[]{400,200};
        c2.momentum = new double[]{-100,0};

        Circle c3 = new Circle();
        c3.name = "c3";
        c3.mass = 3;
        c3.radius = 15;
        c3.position = new double[]{200,230};
        c3.momentum = new double[]{200,0};

        Box floor = new Box();
        floor.name = "floor";
        floor.isStatic = true;
        floor.height = 20;
        floor.width = 500;
        floor.position = new double[]{350,10};

        Box ceiling = new Box();
        ceiling.name = "ceiling";
        ceiling.isStatic = true;
        ceiling.height = 20;
        ceiling.width = 500;
        ceiling.position = new double[]{350,400};

        Box[] boxes;
        Circle[] circles;
        Shapes[] objects;

        if (enableInitDialogue) {

            Scanner scanner = new Scanner(System.in);

            System.out.print("How many boxes do you want? ");
            boxes = new Box[scanner.nextInt()];
            for (int i = 0; i < boxes.length; i++) {
                boxes[i] = new Box();
            }

            for (int i = 0; i < boxes.length; i++) {
                System.out.print("Is box " + (i + 1) + " static? (true or false): ");
                boxes[i].isStatic = scanner.nextBoolean();

                System.out.print("Height and width of box " + (i + 1) + ": ");
                boxes[i].height = scanner.nextInt();
                boxes[i].width = scanner.nextInt();

                System.out.print("Position of Box" + (i + 1) + ": ");
                boxes[i].position = new double[]{scanner.nextInt(), scanner.nextInt()};

                //for mass of boxes
                System.out.print("Mass of Box" + (i + 1) + ": ");
                boxes[i].mass = scanner.nextDouble();

                System.out.print("Initial velocity of box" + (i + 1) + ": ");
                boxes[i].momentum = new double[]{scanner.nextInt() * boxes[i].mass, scanner.nextInt() * boxes[i].mass};

                //for friction of boxes
                System.out.print("Friction of Box" + (i + 1) + ": ");
                boxes[i].friction = scanner.nextDouble();

                //for bounce of boxes
                System.out.print("Bounce of Box" + (i + 1) + ": ");
                boxes[i].bounce = scanner.nextDouble();


            }


            System.out.println("How many circles do you want? ");
            circles = new Circle[scanner.nextInt()];
            for (int i = 0; i < circles.length; i++) {
                circles[i] = new Circle();
            }
            for (int i = 0; i < circles.length; i++) {
                System.out.print("Radius of circle " + (i + 1) + ": ");
                circles[i].radius = scanner.nextInt();

                //for mass of circle
                System.out.print("Mass of Circle" + (i + 1) + ": ");
                circles[i].mass = scanner.nextDouble();

                //for friction of circle
                System.out.print("Friction of Circle" + (i + 1) + ": ");
                circles[i].friction = scanner.nextDouble();

                //for bounce of circle
                System.out.print("Bounce of Circle" + (i + 1) + ": ");
                circles[i].bounce = scanner.nextDouble();
            }


            // Create new init loop:
            // How many boxes do you want?
            // take input and create box array of that length.
            // loop through new array, and use user input to init each field.

            //Example
            // C: How many boxes would you like?
            // U: 3
            // *Creates new array length 3
            // C: Mass of box 1?
            // U: 2
            // C: x and y position of box 1?
            // u: 100 300
            // C: velocity of box 1?
            // U: 20 0
            // C: Mass of Box 2?
            //.... repeat for all boxes


            //Master Array
            //This code combines the two separate circle and box arrays into one master array
            objects = new Shapes[boxes.length + circles.length];
            for (int i = 0; i < boxes.length; i++) {
                objects[i] = boxes[i];
            }
            for (int i = boxes.length; i < objects.length; i++) {
                objects[i] = circles[i - boxes.length];
            }

            for (int i = 0; i < objects.length; i++) {
                System.out.println(objects[i].mass);
            }
        } else {
            //Master object array

            objects = new Shapes[]{c1, c2, c3, floor, ceiling};


            int boxArrayLength = 0;
            for (int i = 0; i < objects.length; i++) if (objects[i] instanceof Box) boxArrayLength++;
            boxes = new Box[boxArrayLength];
            int boxesIndex = 0;
            for (int i = 0; boxesIndex < boxArrayLength; i++) {
                if (objects[i] instanceof Box) {
                    boxes[boxesIndex] = (Box)objects[i];
                    boxesIndex++;
                }
            }
            int circleArrayLength = 0;
            for (int i = 0; i < objects.length; i++) if (objects[i] instanceof Circle) circleArrayLength++;
            circles = new Circle[circleArrayLength];
            int circlesIndex = 0;
            for (int i = 0; circlesIndex < circleArrayLength; i++) {
                if (objects[i] instanceof Circle) {
                    circles[circlesIndex] = (Circle) objects[i];
                    circlesIndex++;
                }
            }

        }

        //Drawing Board initialization
        DrawingPanel panel = Render.initPanel();
        Graphics g = Render.initGraphics(panel);



        //create sub arrays
        // This stupid code sorts the master array into sub arrays of each object type. I will make working with large numbers of objects easier.
        // It also removes the need to check for specific object type when resolving collisions.

//        //test to make sure sorting worked
//        System.out.println("Circles:");
//        for (int i = 0; i < circles.length; i++) {
//            System.out.println(circles[i].name);
//        }
//        System.out.println("Boxes:");
//        for (int i = 0; i < boxes.length; i++) {
//            System.out.println(boxes[i].name);
//        }



        //Main Loop
        for (int steps = 0; steps < MAX_STEPS; steps++) {

            //Apply force of gravity
            if (GRAVITY) {
                for (int i = 0; i < objects.length; i++) {
                    if (!objects[i].isOnFloor && !objects[i].isStatic) {
                        objects[i].forceActing[1] = -objects[i].mass * 9.8;
                    }
                }
            }

            for (int k = 0; k < objects.length; k++) objects[k].isOnFloor = false;

            // Box collision detection
            for (int i = 0; i < boxes.length - 1; i++) {
                for (int j = i + 1; j < boxes.length; j++) {
                    if (Math.abs(boxes[i].position[1] - boxes[j].position[1]) <= (boxes[i].height / 2.0 + boxes[j].height / 2.0) &&
                            Math.abs(boxes[i].position[0] - boxes[j].position[0]) <= (boxes[i].width / 2.0 + boxes[j].width / 2.0)) {
                        int collisionDirection;
                        if ((Math.abs(boxes[i].position[1]-boxes[j].position[1]) >= Math.abs(boxes[i].position[0]-boxes[j].position[0]) || boxes[i].isStatic || boxes[j].isStatic)) {
                            collisionDirection = 1;
                        } else {
                            collisionDirection = 0;
                        }

                        if ((Op.vectorDotProduct(boxes[i].momentum, boxes[j].momentum) < 0) || boxes[i].isStatic || boxes[j].isStatic) {
                            boxes[i].momentum[collisionDirection] *= -1 * boxes[i].bounce;
                            boxes[i].position = Op.vectorAdditionD(boxes[i].position, Op.scalarMultiplyD(Op.scalarMultiplyD(boxes[i].momentum, DT), boxes[i].mass));
                            boxes[j].momentum[collisionDirection] *= -1 * boxes[j].bounce;
                            boxes[j].position = Op.vectorAdditionD(boxes[j].position, Op.scalarMultiplyD(Op.scalarMultiplyD(boxes[j].momentum, DT), boxes[j].mass));
                            boxes[i].isOnFloor = true;
                            boxes[j].isOnFloor = true;
                        } else {
                            int fastOne;
                            int slowOne;
                            if (boxes[i].momentum[collisionDirection] >= boxes[j].momentum[collisionDirection]) {
                                fastOne = i;
                                slowOne = j;
                            } else {
                                fastOne = j;
                                slowOne = i;
                            }
                            boxes[slowOne].momentum[collisionDirection] += boxes[fastOne].momentum[collisionDirection];
                            boxes[fastOne].momentum[collisionDirection] = 0;
                        }

                    }
                }
            }
            //circle on box vertical collision
            for (int i = 0; i < circles.length; i++) {
                for (int j = 0; j < boxes.length; j++) {
                    if (Math.abs(circles[i].position[1] - boxes[j].position[1]) <= (circles[i].radius + boxes[j].height/2.0) &&
                            Math.abs(circles[i].position[0] - boxes[j].position[0]) < (circles[i].radius + boxes[j].width/2.0)) {
                        circles[i].isOnFloor = true;
                        boxes[j].isOnFloor = true;

                        if (!circles[i].isStatic) {
                            circles[i].momentum[1] *= -1 * circles[i].bounce;
                            circles[i].position = Op.vectorAdditionD(circles[i].position, Op.scalarMultiplyD(Op.scalarMultiplyD(circles[i].momentum, DT), circles[i].mass));

                        }
                        if (!boxes[j].isStatic) {
                            boxes[j].momentum[1] *= -1 * boxes[j].bounce;
                            boxes[j].position = Op.vectorAdditionD(boxes[j].position, Op.scalarMultiplyD(Op.scalarMultiplyD(boxes[j].momentum, DT), boxes[j].mass));
                        }

                    }
                }
            }
            //Circle on circle
            for (int i = 0; i < circles.length - 1; i++) {
                if (circles[i].collisionImmunity > 0) continue;
                for (int j = i + 1; j < circles.length; j++) {
                    if (circles[j].collisionImmunity > 0) continue;
                    double[] T = {circles[i].position[0]-circles[j].position[0], circles[i].position[1]-circles[j].position[1]};
                    if (Op.vectorMag(T) <= (circles[i].radius+circles[j].radius)) {
                        System.out.println("begin collison");

                        // i x
                        //circles[i].position[0] = ((circles[i].mass-circles[j].mass)/(circles[i].mass+circles[j].mass)) * circles[i].
//                        double[] vi = Op.scalarMultiplyD(circles[i].momentum, 1/circles[i].mass);
//                        double[] vj = Op.scalarMultiplyD(circles[j].momentum, 1/circles[j].mass);
//
//                        circles[i].momentum = Op.vectorAdditionD(Op.scalarMultiplyD(vi, circles[i].mass *((circles[i].mass-circles[j].mass)/(circles[i].mass+circles[j].mass))), Op.scalarMultiplyD(vj, circles[i].mass * ((2*circles[j].mass)/(circles[i].mass+circles[j].mass))));
//                        circles[i].momentum = Op.vectorAdditionD(Op.scalarMultiplyD(vj, circles[i].mass *((circles[j].mass-circles[i].mass)/(circles[i].mass+circles[j].mass))), Op.scalarMultiplyD(vi, circles[i].mass * ((2*circles[i].mass)/(circles[i].mass+circles[j].mass))));
                        double vx1 = circles[i].momentum[0] / circles[i].mass;
                        double vy1 = circles[i].momentum[1] / circles[i].mass;
                        double[] v1_B = new double[]{vx1, vy1};

                        double vx2 = circles[j].momentum[0] / circles[j].mass;
                        double vy2 = circles[j].momentum[1] / circles[j].mass;
                        double[] v2_B = new double[]{vx2, vy2};

                        //convert to collision basis
                        double[] P = new double[]{T[1], -1 * T[0]};
                        Op.printArrayD(P);
                        System.out.println();
                        double[][] C = new double[][]{{P[0],T[0]},{P[1],T[1]}};
                        Op.printMatrix(C);
                        System.out.println();
                        double[][] Cinv = Op.matrixInverse2d(C);
                        Op.printMatrix(Cinv);
                        System.out.println();

                        Op.printMatrix(Op.matrixDotProduct(C,Cinv));

                        double[] v1_C = Op.matrixVectorDotProduct(Cinv, v1_B);
                        double[] v2_C = Op.matrixVectorDotProduct(Cinv, v2_B);
                        Op.printArrayD(v1_C);
                        System.out.println();
                        Op.printArrayD(v2_C);
                        System.out.println();

//                        double v1f_C = v1_C[1] * -1;
//                        double v2f_C = v2_C[1] * -1;
                        double v1f_C = ((circles[i].mass - circles[j].mass)/(circles[i].mass + circles[j].mass))*v1_C[1] + (2*circles[j].mass/(circles[i].mass + circles[j].mass))*v2_C[1];
                        double v2f_C = ((circles[j].mass - circles[i].mass)/(circles[i].mass + circles[j].mass))*v2_C[1] + (2*circles[i].mass/(circles[i].mass + circles[j].mass))*v1_C[1];

                        v1_C[1] = v1f_C;
                        v2_C[1] = v2f_C;

                        v1_B = Op.matrixVectorDotProduct(C, v1_C);
                        v2_B = Op.matrixVectorDotProduct(C, v2_C);

                        circles[i].momentum = Op.scalarMultiplyD(v1_B, circles[i].mass);
                        circles[j].momentum = Op.scalarMultiplyD(v2_B, circles[j].mass);

                        Op.printArrayD(circles[i].momentum);





//                        circles[i].momentum[0] = circles[i].mass*( ( ( (circles[i].mass - circles[j].mass) / (circles[i].mass + circles[j].mass) ) * vx1) + ((2*circles[j].mass/(circles[i].mass + circles[j].mass)) * vx2 ) );
//                        circles[i].momentum[1] = circles[i].mass*( ( ( (circles[i].mass - circles[j].mass) / (circles[i].mass + circles[j].mass) ) * vy1) + ((2*circles[j].mass/(circles[i].mass + circles[j].mass)) * vy2 ) );
//
//
//                        circles[j].momentum[0] = circles[i].mass*( ( ( (circles[j].mass - circles[i].mass) / (circles[i].mass + circles[j].mass) ) * vx2) + ((2*circles[i].mass/(circles[i].mass + circles[j].mass)) * vx1 ) );
//                        circles[j].momentum[1] = circles[i].mass*( ( ( (circles[j].mass - circles[i].mass) / (circles[i].mass + circles[j].mass) ) * vy2) + ((2*circles[i].mass/(circles[i].mass + circles[j].mass)) * vy1 ) );

                        System.out.println("Collision Detected");
                        circles[i].collisionImmunity = DEFAULT_COLLISION_IMMUNITY;
                        circles[j].collisionImmunity = DEFAULT_COLLISION_IMMUNITY;
                    }
                }
            }

            // decrement collision immunity
            for (int i = 0; i < objects.length; i++) {
                if (objects[i].collisionImmunity > 0) objects[i].collisionImmunity--;
            }




            //Move Objects
            for (int i = 0; i < objects.length; i++) {
                if (objects[i].isStatic) {
                    objects[i].momentum = new double[]{0,0};
                    objects[i].forceActing = new double[]{0,0};
                }

                // I converted everything into acceleration and velocity before making movement calculation
                // I'm sure there is a more elegant way to do this.
                // I was originally going to generalize everything to n-dimensions but I am too lazy so everything for here on is hard coded for 2d

                if (objects[i].momentum[1] < 1 && objects[i].isOnFloor) {
                    objects[i].momentum[1] = 0;
                    objects[i].forceActing[1] = 0;
                }

                double[] a = {0,0};
                a[0] = objects[i].forceActing[0]/objects[i].mass;
                a[1] = objects[i].forceActing[1]/objects[i].mass;

                double[] v = {0,0};
                v[0] = objects[i].momentum[0]/objects[i].mass;
                v[1] = objects[i].momentum[1]/objects[i].mass;

                objects[i].position[0] += v[0]*DT + 0.5*Math.pow(DT,2)*a[0];
                objects[i].momentum[0] += objects[i].forceActing[0]*DT;

                objects[i].position[1] += v[1]*DT + 0.5*Math.pow(DT,2)*a[1];
                objects[i].momentum[1] += objects[i].forceActing[1]*DT;

                objects[i].forceActing = new double[]{0,0};

            }


            // Render Objects
            panel.clear();
            for (int i = 0; i < objects.length; i++) Render.drawObject(g, objects[i]);
            panel.sleep(10);

//            System.out.println("c1: " + c1.isOnFloor);
//            System.out.println("c2: " + c2.isOnFloor);
//            System.out.println("b1: " + b1.isOnFloor);
        } //end of main loop
    }
}