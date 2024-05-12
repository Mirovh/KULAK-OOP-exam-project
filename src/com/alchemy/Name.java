package com.alchemy;

import be.kuleuven.cs.som.annotate.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing the name of an alchemic ingredient.
 *
 * Defensively programmed.
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
 */
public class Name {

    private final static String ingredientPartRegex = "^[a-zA-Z'\\(\\)\\s]*$";
    private final static String[] blacklistedWordsIngredientPart = {"mixed", "with","Heated","Cooled"};
    private String[] blacklistedWords = {};

    private String[] nameParts = new String[0];
    private List<String> prefixes = new ArrayList<>();
    private List<String> suffixes = new ArrayList<>();
    private String specialName = null;

    
    /**********************************************************
     * Constructors
     **********************************************************/

    public Name(String name, String specialName, String[] blacklistedWords) throws IllegalNameException, IllegalSpecialNameException {
        this.blacklistedWords = blacklistedWords;
        setName(name);
        if (specialName != null) {
            setSpecialName(specialName);
        }
    }

    public Name(String name, String[] blacklistedWords) throws IllegalNameException {
        this.blacklistedWords = blacklistedWords;
        setName(name);
    }

    public Name(String name, String specialName) throws IllegalNameException, IllegalSpecialNameException {
        this(name, specialName, new String[0]);
    }

    public Name(String name) throws IllegalNameException {
        this(name, new String[0]);
    }
    

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    /**
     * Set the name of the ingredient to the given name.
     *
     * @param name The name to set.
     * @throws IllegalNameException If the given name is not a valid mixture name.
     * @post The name of the ingredient is set to the given name.
     *       | new.getName() == name
     */
    @Raw
    public void setName(String name) throws IllegalNameException {
        if (isValidMixtureName(name)) {
            this.nameParts = name.split(" mixed with ");
        } else {
            throw new IllegalNameException(name);
        }
    }

    /**
     * Set the special alias name of the ingredient to the given name.
     * The special name is used to refer to a mixture of ingredients.
     * The special name must be a valid ingredient part name.
     * 
     * @param name The special name to set.
     * @throws IllegalSpecialNameException if the special name is invalid or the ingredient does not have enough name parts
     * @post The special name of the ingredient is set to the given name.
     *      | new.getSpecialName() == name
     */
    @Raw
    public void setSpecialName(String name) throws IllegalSpecialNameException {
        if (isValidIngredientPartName(name) && this.nameParts.length > 1) {
            this.specialName = name;
        } else {
            throw new IllegalSpecialNameException(name);
        }
    }

    /**
     * Get the basic name of the ingredient. (without pre- and suffixes)
     *
     * @return The name of the ingredient without pre- and suffixes.
     */
    @Basic
    public String getBasicName() {
        return String.join(" mixed with ", nameParts);
    }

    /**
     * Get the special alternative name of the ingredient (mixture).
     *
     * @return The special name of the ingredient.
     */
    @Basic
    public String getSpecialName() {
        return specialName;
    }

    /**
     * Get the full name of the ingredient. 
     * (special name followed by basic name with pre- and suffixes, or just the basic name with pre- and suffixes if no special name is set.)
     *
     * @return The full name of the ingredient.
     */
    @Basic
    public String getFullName() {
        if (specialName != null) {
            return specialName + " (" + addPreAndSuffixes(getBasicName()) + ")";
        } else {
            return addPreAndSuffixes(getBasicName());
        }
    }

    /**
     * Add a prefix to the name of the ingredient.
     *
     * @param prefix The prefix to add.
     */
    public void addPrefix(String prefix) {
        prefixes.add(prefix);
    }

    /**
     * Add a suffix to the name of the ingredient.
     *
     * @param suffix The suffix to add.
     */
    public void addSuffix(String suffix) {
        suffixes.add(suffix);
    }

    /**
     * Remove a prefix from the name of the ingredient.
     *
     * @param prefix The prefix to remove.
     */
    public void removePrefix(String prefix) {
        prefixes.remove(prefix);
    }

    /**
     * Remove a suffix from the name of the ingredient.
     *
     * @param suffix The suffix to remove.
     */
    public void removeSuffix(String suffix) {
        suffixes.remove(suffix);
    }

    /**
     * Get all prefixes of the name of the ingredient.
     *
     * @return The prefixes of the name of the ingredient.
     */
    public List<String> getPrefixes() {
        return prefixes;
    }

    /**
     * Get all suffixes of the name of the ingredient.
     *
     * @return The suffixes of the name of the ingredient.
     */
    public List<String> getSuffixes() {
        return suffixes;
    }

    /**********************************************************
     * Helper methods
     **********************************************************/

    /**
     * Add pre- and suffixes to the given name.
     * 
     * @param name The name to add pre- and suffixes to.
     * @return The name with pre- and suffixes added.
     */
    private String addPreAndSuffixes(String name) {
        return name; // TODO: implement
    }


    /**********************************************************
     * Validity checks
     **********************************************************/

    /**
     * Checks if the given mixture name is valid.
     * A valid mixture name consists of multiple ingredient part names separated by " mixed with ".
     * Each ingredient part should have every word capitalized and be at least 2 characters long.
     * If the name consists of only one part, it should be at least 3 characters long.
     * The name must not contain any blacklisted words. (This includes pre- and suffixes.)
     *
     * @param name The mixture name to be checked.
     * @return True if the mixture name is valid, false otherwise.
     */
    private boolean isValidMixtureName(String name) {
        // the name mustn't contain any blacklisted words
        for (String word : blacklistedWords) {
            if (name.contains(word)) return false;
        }
        // split the name into parts
        String[] parts = name.split(" mixed with ");
        // if there is only one part, it has to be at least 3 characters long
        if (name.length() < 3) return false;
        // each part has to be a valid ingredient part name
        for (String part : parts) {
            if (!isValidIngredientPartName(part)) return false;
        }
        // all parts combined with ' mixed with ' should equal the original name
        return String.join(" mixed with ", parts).equals(name);
    }

    /**
     * Checks if the given ingredient part name is valid.
     * A valid ingredient part name must match the ingredient part regex and must not be empty.
     * Each word must start with a capital letter and be at least 2 characters long.
     * All other characters must be lowercase.
     * The name must not contain any blacklisted words. (eg. "mixed" or "with")
     *
     * @param name The ingredient part name to be checked.
     * @return True if the ingredient part name is valid, false otherwise.
     */
    private boolean isValidIngredientPartName(String name) {
        // the name mustn't contain any blacklisted words
        for (String word : blacklistedWordsIngredientPart) {
            if (name.contains(word)) return false;
        }
        for (String word : blacklistedWords) {
            if (name.contains(word)) return false;
        }
        // the name must match the ingredient part regex and must not be empty
        if (!name.matches(ingredientPartRegex) || name.isEmpty()) {
            return false;
        }
        // each word must not start with a lowercase letter and be at least 2 characters long
        String[] words = name.split(" ");
        for (String word : words) {
            if (Character.isLowerCase(word.charAt(0)) ||
                    word.length() < 2) {
                return false;
            }
            // all other characters must be lowercase
            for (int i = 1; i < word.length(); i++) {
                if (Character.isUpperCase(word.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }


    /**********************************************************
     * Exceptions
     **********************************************************/

    /**
     * Exception thrown when an invalid mixture name is provided.
     */
    public static class IllegalNameException extends Exception {
        /**
         * Constructs an IllegalNameException with the given name.
        *
        * @param name The invalid mixture name.
        */
        IllegalNameException(String name) {
            super("The name '" + name + "' is not a valid mixture name.");
        }
    }

    /**
     * Exception thrown when an invalid special name is provided.
     */
    public static class IllegalSpecialNameException extends Exception {
        /**
         * Constructs an IllegalSpecialNameException with the given name.
        *
        * @param name The invalid special name.
        */
        IllegalSpecialNameException(String name) {
            super("The name '" + name + "' could not be set as a special name.");
        }
    }
}
