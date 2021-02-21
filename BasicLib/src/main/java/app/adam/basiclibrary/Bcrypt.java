package app.adam.basiclibrary;

import at.favre.lib.crypto.bcrypt.BCrypt;

class Bcrypt{
    public boolean onCheck(String text, String hash){
        return BCrypt.verifyer().verify(text.toCharArray(), hash).verified;
    }
    public String onHash(String text, int length){
        return BCrypt.withDefaults().hashToString(length, text.toCharArray());
    }
}
