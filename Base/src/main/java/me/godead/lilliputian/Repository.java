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

public enum Repository {

    JITPACK("https://jitpack.io/%s.jar"),
    MAVENCENTRAL("https://repo1.maven.org/maven2/%s.jar"),
    SONATYPE("https://search.maven.org/remotecontent?filepath=%s.jar"),
    CUSTOM("repo/%s.jar");

    private final String repositoryURL;

    Repository(String repositoryURL) {
        this.repositoryURL = repositoryURL;
    }

    public String getRepositoryURL() {
        return repositoryURL;
    }
}