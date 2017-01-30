William Kurek
CS 1538
Homework #1

HOW TO RUN PROGRAM:

    1. Ensure the following source files are in the same directory:
        RobotSimulation.java
        Robot.java
        Map.java
        Coordinate.java
        ProbabilityCompare.java
        Statistics.java
        
    2. On the command line (tested on a Windows OS) execute the following commands:   
        "javac RobotSimulation.java"
        "java RobotSimulation (.csv file) (strategy number)"
            i.e. "java RobotSimulation hw1map.csv 1"
            
            STRATEGY NUMBERS:
            1. Move toward the location where the material might appear (and reappear)
            with the highest probability and ramain there for the rest of the time.
            2. Randomly choose a direction and continue uutil the robot bumps into a wall/obstacle.
            3. Randomly choose a direction at every step.
            4. Always head toward a (randomly chosen) location where the material could appear.
            
REFERENCES:


OVERVIEW:
    The goal of this experiment is to determine whether one of the strategies is better 
    than the others for the given map and if changing environmental conditions can alter those
    results. I believe strategy 4 will be the best because it is constantly moving towards 
    a location that can generate a material, while potentially harvesting other materials
    on the way.
    
EXPERIMENTAL DESIGN:

ANALYSIS:

FURTHER DISCUSSIONS:

            
         


