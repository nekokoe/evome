/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.server;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.shared.UConsts;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;

/**
 * This is an example of how to use UploadAction class.
 *  
 * This servlet saves all received files in a temporary folder, 
 * and deletes them when the user sends a remove request.
 * 
 * @author Manolo Carrasco Moñino
 *
 */
public class FileUploadServlet extends UploadAction {

        
    private static final long serialVersionUID = 1L;
    HashMap<String, String> receivedContentTypes = new HashMap<String, String>();
    /**
     * Maintain a list with received files and their content types. 
     */
    HashMap<String, File> receivedFiles = new HashMap<String, File>();

    /**
     * Override executeAction to save the received files in a custom place
     * and delete this items from session.  
     */
    @Override
    public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
        String response = null;
        ArrayList<String> responseList = new ArrayList<String>();
        int cont = 0;
        for (FileItem item : sessionFiles) {
            if (false == item.isFormField()) {
                cont++;
                try {
                    
                    //Create a new file based on the remote file name in the client
//                    String saveName = item.getName().replaceAll("[\\\\/><\\|\\s\"'{}()\\[\\]]+", "_");
//                    String prefix = new RandStr().getRandStr();
//                    String upFile = prefix + ".fasta";
//                    String fileName = item.getFieldName();
//                    item.setFieldName("nuc"+fileName+".fasta");
//                    File file =new File(dirFile + "/" + fileName);
                    // Create a temporary file placed in /tmp (only works in unix)
//                    File file = File.createTempFile("upload-", ".bin", new File("/tmp"));

                    // Create a temporary file placed in the default system temp folder
                    File file = File.createTempFile("upload-", ".fasta");
                    item.write(file);

                    /// Save a list with the received files
                    receivedFiles.put(item.getFieldName(), file);
                    receivedContentTypes.put(item.getFieldName(), item.getContentType());

                    /// Send a customized message to the client.
                    //responseList.add(file.getAbsolutePath());
                    response = file.getAbsolutePath();
                } catch (Exception e) {
                    throw new UploadActionException(e);
                }
            }
        }

        /// Remove files from session because we have a copy of them
        removeSessionFileItems(request);

        /// Send your customized message to the client.
        //response = StringUtils.join(responseList.iterator(), "::");
        
        return response; // only return the file path on server        
    }

    /**
     * Get the content of an uploaded file.
     */
    @Override
    public void getUploadedFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fieldName = request.getParameter(UConsts.PARAM_SHOW);
        File f = receivedFiles.get(fieldName);
        if (f != null) {
            response.setContentType(receivedContentTypes.get(fieldName));
            FileInputStream is = new FileInputStream(f);
            copyFromInputStreamToOutputStream(is, response.getOutputStream());
        } else {
            renderXmlResponse(request, response, XML_ERROR_ITEM_NOT_FOUND);
        }
    }

    /**
     * Remove a file when the user sends a delete request.
     */
    @Override
    public void removeItem(HttpServletRequest request, String fieldName) throws UploadActionException {
        File file = receivedFiles.get(fieldName);
        receivedFiles.remove(fieldName);
        receivedContentTypes.remove(fieldName);
        if (file != null) {
            file.delete();
        }
    }
}