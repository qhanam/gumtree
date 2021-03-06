package fr.labri.gumtree.client.batch;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.labri.gumtree.client.TreeGeneratorRegistry;
import fr.labri.gumtree.tree.DigestGenerator;
import fr.labri.gumtree.tree.Tree;
import fr.labri.gumtree.tree.TreeUtils;

public class DigestProcessor extends AbstractFileProcessor {
	
	private int stdTime;
	
	private int rStdTime;
	
	private int md5Time;
	
	private int rMd5Time;
	
	private int rRdmTime;
	
	private Map<Integer,Set<String>> stdDigests;
	
	private Map<Integer,Set<String>> rStdDigests;
	
	private Map<Integer,Set<String>> md5Digests;
	
	private Map<Integer,Set<String>> rMd5Digests;
	
	private Map<Integer,Set<String>> rRdmDigests;
	
	public static void main(String[] args) {
		DigestProcessor g = new DigestProcessor(args[0],"/home/falleri/Out/");
		g.process();
	}

	public DigestProcessor(String inFolder, String outFolder) {
		super(inFolder, outFolder);
	}
	
	@Override
	public void init() {
		stdTime = 0;
		rStdTime = 0;
		md5Time = 0;
		rMd5Time = 0;
		rRdmTime = 0;
		stdDigests = new HashMap<>();
		rStdDigests = new HashMap<>();
		md5Digests = new HashMap<>();
		rMd5Digests = new HashMap<>();
		rRdmDigests = new HashMap<>();
	}

	@Override
	public void process(String file) throws IOException {
		Tree tree = TreeGeneratorRegistry.getInstance().getTree(file);
		long tic = tic();
		TreeUtils.computeDigest(tree, new DigestGenerator.StdHashGenerator());
		stdTime += tic() - tic;
		updateDigests(tree, stdDigests);
		
		tic = tic();
		TreeUtils.computeDigest(tree, new DigestGenerator.RollingStdHashGenerator());
		rStdTime += tic() - tic;
		updateDigests(tree, rStdDigests);
		
		tic = tic();
		TreeUtils.computeDigest(tree, new DigestGenerator.Md5HashGenerator());
		md5Time += tic() - tic;
		updateDigests(tree, md5Digests);
		
		tic = tic();
		TreeUtils.computeDigest(tree, new DigestGenerator.RollingMd5HashGenerator());
		rMd5Time += tic() - tic;
		updateDigests(tree, rMd5Digests);
		
		tic = tic();
		TreeUtils.computeDigest(tree, new DigestGenerator.RollingMd5HashGenerator());
		rRdmTime += tic() - tic;
		updateDigests(tree, rRdmDigests);
	}
	
	private void updateDigests(Tree tree, Map<Integer,Set<String>> digests) {
		for (Tree t: tree.getTrees()) {
			int digest = t.getDigest();
			if (!digests.containsKey(digest)) digests.put(digest, new HashSet<String>());
			digests.get(digest).add(t.toDigestTreeString());
		}
	}
	
	@Override
	public void finish() {
		LOGGER.info("Results for standard hash:");
		result(stdTime, stdDigests);
		LOGGER.info("Results for rolling standard hash:");
		result(rStdTime, rStdDigests);
		LOGGER.info("Results for md5 hash:");
		result(md5Time, md5Digests);
		LOGGER.info("Results for rolling md5 hash:");
		result(rMd5Time, rMd5Digests);
		LOGGER.info("Results for rolling random hash:");
		result(rRdmTime, rRdmDigests);
	}
	
	private void result(int time, Map<Integer,Set<String>> digests) {
		LOGGER.info(String.format("Total time: %d", time));
		int collisions = 0;
		for (int digest: digests.keySet()) {
			if (digests.get(digest).size() > 1) {
				LOGGER.fine("Collision detected for digest: " + digest);
				LOGGER.fine(digests.get(digest).toString());
				collisions += digests.get(digest).size() - 1;
			}
		}
		LOGGER.info(String.format("Total collisions: %d", collisions));
	}

}