package org.iplantc.de.server;

import java.rmi.RemoteException;

import org.swift.common.soap.confluence.AuthenticationFailedException;
import org.swift.common.soap.confluence.InvalidSessionException;
import org.swift.common.soap.confluence.NotPermittedException;
import org.swift.common.soap.confluence.RemoteComment;
import org.swift.common.soap.confluence.RemotePage;
import org.swift.confluence.cli.ConfluenceClient;

/**
 * A subclass of ConfluenceClient that adds methods for adding and updating tool ratings.
 * 
 * @author hariolf
 * 
 */
public class IPlantConfluenceClient extends ConfluenceClient {
    private String address;
    private String user;
    private String password;

    /**
     * Creates a new IPlantConfluenceClient. Subsequent calls use the provided address/user/password.
     * 
     * @param address the base Confluence URL
     * @param user the Confluence user
     * @param password the Confluence password
     */
    public IPlantConfluenceClient(String address, String user, String password) {
        this.address = address;
        this.user = user;
        this.password = password;
    }

    /**
     * Adds a comment to an existing Confluence page and returns an object containing the new comment's
     * ID, etc.
     * 
     * @param space the Confluence space the page lives in
     * @param pageTitle the title of the page to add a comment to
     * @param text the comment text
     * @return a RemoteComment instance
     * @throws RemoteException
     * @throws ClientException
     */
    public RemoteComment addComment(String space, String pageTitle, String text)
            throws RemoteException, ClientException {
        login(address, user, password);

        RemotePage page = getPage(pageTitle, space);
        RemoteComment comment = new RemoteComment();
        comment.setPageId(page.getId());
        comment.setContent(text);
        comment = service.addComment(token, comment);
        return comment;
    }

    /**
     * Changes an existing comment.
     * 
     * @param newComment the new comment; must have the correct ID and service address set
     * @throws InvalidSessionException
     * @throws NotPermittedException
     * @throws org.swift.common.soap.confluence.RemoteException
     * @throws RemoteException
     * @throws ClientException
     */
    public void editComment(RemoteComment newComment)
            throws InvalidSessionException, NotPermittedException,
            org.swift.common.soap.confluence.RemoteException, RemoteException, ClientException {
        login(address, user, password);

        service.editComment(token, newComment);
    }

    /**
     * Logs a user into Confluence and sets the authentication token.
     * 
     * @param address base Confluence URL
     * @param user
     * @param password
     * @throws AuthenticationFailedException
     * @throws org.swift.common.soap.confluence.RemoteException
     * @throws RemoteException
     * @throws ClientException
     */
    private void login(String address, String user, String password)
            throws AuthenticationFailedException,
            org.swift.common.soap.confluence.RemoteException, RemoteException, ClientException {
        ExitCode code = doWork(new String[] {"-a", "login", "--server", address, "--user", user, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                "--password", password}); //$NON-NLS-1$
        if (code != ExitCode.SUCCESS)
            throw new ClientException("doWork() returned exit code " + code); //$NON-NLS-1$
    }
}