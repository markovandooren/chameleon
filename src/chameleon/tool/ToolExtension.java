package chameleon.tool;

import chameleon.core.language.Language;

/**
 * Created for experimentation
 * User: koenvdk
 * Date: 16-okt-2006
 * Time: 13:14:40
 */
public interface ToolExtension extends Cloneable {

    Language getLanguage();
    void setLanguage(Language lang, Class<? extends ToolExtension> toolExtensionClass);
    ToolExtension clone();

}
