package Parser.ASTNodes.Variables;

import Exceptions.FMLExecutionException;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static Util.ObjectUtil.nullOrEqual;

public class ListVariable extends Variable {

    private final List<FileVariable> innerList;

    public ListVariable(String absolutePath) throws FMLExecutionException {
        this.innerList = buildInnerList(absolutePath);
    }

    public List<FileVariable> getInnerList() {
        return innerList;
    }

    private List<FileVariable> buildInnerList(String absolutePath) throws FMLExecutionException {
        File[] files = listFiles(absolutePath);
        if (files == null) {
            throw new FMLExecutionException("Cannot create file list. The given path is invalid: " + absolutePath);
        }

        List<FileVariable> innerList = new LinkedList<>();
        for (File file: files) {
            innerList.add(new FileVariable(file.getAbsolutePath()));
        }
        return innerList;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ListVariable)) {
            return false;
        }
        ListVariable other = (ListVariable) obj;
        return nullOrEqual(this.innerList, other.innerList);
    }

    private File[] listFiles(String absolutePath) {
        if (!absolutePath.contains("*")) {
            return new File[]{new File(absolutePath)};
        }
        String tempPath = absolutePath.replace("*", "_wildcard_");
        tempPath = Paths.get(".", tempPath).toAbsolutePath().toString();
        File parent = Paths.get(tempPath).getParent().toFile();
        Path wildcardReplaced = Paths.get(tempPath).getFileName();
        String wildcard = wildcardReplaced.toString().replace("_wildcard_", "*");
        FileFilter wildCardFilter = new WildcardFileFilter(wildcard);
        return parent.listFiles(wildCardFilter);
    }
}
