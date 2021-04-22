/*
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 */

/*
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package me.godead.lilliputian;

import java.io.*;
import java.net.*;
import java.nio.channels.Channels;

public class Dependency {

    private final Repository repository;

    private final String group;

    private final String name;

    private final String version;

    private final Boolean customRepository;

    private final String repositoryURL;
    private final DependencyLoader loader = new DependencyLoader((URLClassLoader) Lilliputian.getPlugin().getClass().getClassLoader());

    public Dependency(Repository repository, String group, String name, String version) {
        this.repositoryURL = null;
        this.customRepository = false;
        this.repository = repository;
        this.group = group;
        this.name = name;
        this.version = version;
    }

    public Dependency(String repositoryURL, String group, String name, String version) {
        this.repositoryURL = repositoryURL;
        this.customRepository = true;
        this.repository = Repository.CUSTOM;
        this.group = group;
        this.name = name;
        this.version = version;
    }

    private String getJarName() {
        return name + "-" + version + ".jar";
    }

    private String getDownloadURL() {
        final String repo = String.format(repository.getRepositoryURL(), group.replace('.', '/') + "/" + name + "/" + version + "/" + name + "-" + version);

        if (customRepository) {
            assert repositoryURL != null : "Error. Something went wrong! Custom repository URL is null.";
            return repo.replace("repo", repositoryURL);
        }

        return repo;
    }

    protected void downloadAndLoad() {
        // Downloads the dependency
        final File file = download();
        // Then loads it into the class path
        load(file);
    }

    private void load(File file) {
        loader.loadDependency(file);
    }

    private File download() {

        File dir = new File(Lilliputian.getPath());

        if (!dir.exists()) dir.mkdirs();

        File file = new File(Lilliputian.getPath() + "/" + getJarName());

        // Returns if the dependency already exists
        if (file.exists()) return file;

        try (FileOutputStream outputStream = new FileOutputStream(Lilliputian.getPath() + "/" + getJarName())) {
            outputStream.getChannel()
                    .transferFrom(Channels.newChannel(new URL(getDownloadURL())
                            .openStream()), 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}