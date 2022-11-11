package manejadores;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MioObjetos extends ObjectOutputStream {
    public MioObjetos(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {

    }
}
