/*
 This file is part of Subsonic.

 Subsonic is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Subsonic is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Subsonic.  If not, see <http://www.gnu.org/licenses/>.

 Copyright 2009 (C) Sindre Mehus
 */
package github.daneren2005.dsub.domain;

import android.util.Log;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Sindre Mehus
 */
public class Artist implements Serializable {
	private static final String TAG = Artist.class.getSimpleName();

    private String id;
    private String name;
    private String index;
	private boolean starred;
	private int closeness;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
	
	public boolean isStarred() {
		return starred;
	}

	public void setStarred(boolean starred) {
		this.starred = starred;
	}
	
	public int getCloseness() {
		return closeness;
	}
	
	public void setCloseness(int closeness) {
		this.closeness = closeness;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Artist entry = (Artist) o;
		return id.equals(entry.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

    @Override
    public String toString() {
        return name;
    }

	public static class ArtistComparator implements Comparator<Artist> {
		private String[] ignoredArticles;

		public ArtistComparator(String[] ignoredArticles) {
			this.ignoredArticles = ignoredArticles;
		}

		public int compare(Artist lhsArtist, Artist rhsArtist) {
			if("root".equals(lhsArtist.getId())) {
				return 1;
			} else if("root".equals(rhsArtist.getId())) {
				return -1;
			}

			String lhs = lhsArtist.getName().toLowerCase();
			String rhs = rhsArtist.getName().toLowerCase();

			char lhs1 = lhs.charAt(0);
			char rhs1 = rhs.charAt(0);

			if (Character.isDigit(lhs1) && !Character.isDigit(rhs1)) {
				return 1;
			} else if (Character.isDigit(rhs1) && !Character.isDigit(lhs1)) {
				return -1;
			}

			for (String article : ignoredArticles) {
				int index = lhs.indexOf(article.toLowerCase() + " ");
				if (index == 0) {
					lhs = lhs.substring(article.length() + 1);
				}
				index = rhs.indexOf(article.toLowerCase() + " ");
				if (index == 0) {
					rhs = rhs.substring(article.length() + 1);
				}
			}

			return lhs.compareTo(rhs);
		}
	}

	public static void sort(List<Artist> artists, String[] ignoredArticles) {
		try {
			Collections.sort(artists, new ArtistComparator(ignoredArticles));
		} catch (Exception e) {
			Log.w(TAG, "Failed to sort artists", e);
		}
	}
}