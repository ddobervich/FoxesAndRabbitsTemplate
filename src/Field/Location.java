package Field;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Represent a location in a rectangular grid.
 * 
 * @author David J. Barnes and Michael Kolling.  Modified by David Dobervich 2007-2022
 */
public class Location implements Serializable, Comparable<Location>
{
    // Row and column positions.
    private int row; // shut up java
    private int col;

    /**
     * Represent a row and column.
     * @param _row The row.
     * @param _col The column.
     */
    public Location(int _row, int _col)
    {
        row = _row;
        col = _col;
    }
    
    /**
     * Implement content equality.
     */ // This function can be replaced with Comparable<Location> which automatically forces proper object comparison (no need to check object type)
    public boolean equals(@NotNull Object obj)
    {
        if(obj instanceof Location) {
            Location other = (Location) obj;
            return row == other.getRow() && col == other.getCol();
        }
        else {
            return false;
        }
    }

    public int compareTo(@NotNull Location o) {
        if (row == o.row && col == o.col) {
            return 0;
        } else {
            return -1;
        }
    }
    
    /**
     * Return a string of the form row,column
     * @return A string representation of the location.
     */
    public String toString()
    {
        return col + "," + row;
    }



    /**
     * Use the top 16 bits for the row value and the bottom for
     * the column. Except for very big grids, this should give a
     * unique hash code for each (row, col) pair.
     * @return A hashcode for the location.
     */
    public int hashCode()
    {
        return (row << 16) + col;
    }
    
    /**
     * @return The row.
     */
    public int getRow()
    {
        return row;
    }
    
    /**
     * @return The column.
     */
    public int getCol()
    {
        return col;
    }


    // these functions are literally useless????
    /*
     * @return The row.
     *
    public int gety()
    {
        return row;
    }
    

     * @return The column.
     *
    public int getx()
    {
        return col;
    }
     */
}
